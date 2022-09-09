package com.example.demo.service.impl;

import com.example.demo.constant.Role;
import com.example.demo.converter.DateTimeConvert;
import com.example.demo.converter.NewsConverter;
import com.example.demo.converter.SubCommentConverter;
import com.example.demo.domain.Comment;
import com.example.demo.domain.News;
import com.example.demo.domain.SubComment;
import com.example.demo.domain.User;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.SubCommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.customize.NewsRepositoryCustomize;
import com.example.demo.request.comment.CreateCommentRequest;
import com.example.demo.request.news.CreateNewsRequest;
import com.example.demo.request.news.UpdateNewsRequest;
import com.example.demo.request.subcomment.CreateSubCommentRequest;
import com.example.demo.response.ResponseObject;
import com.example.demo.response.comment.CommentResponse;
import com.example.demo.response.news.NewsResponse;
import com.example.demo.response.news.NewsSearchResponse;
import com.example.demo.response.subcomment.SubCommentResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.NewsService;
import com.example.demo.util.JwtData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsRepositoryCustomize newsRepositoryCustomize;
    private final SubCommentRepository subCommentRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;

    public NewsSearchResponse listByPage(int skip, int limit, String hashTag) {
        List<News> list = newsRepositoryCustomize.findAllByHashTag(hashTag, skip, limit);
        List<NewsResponse> collect = list
                .stream()
                .map(n -> {
                    int count = 0;
                    List<Comment> commentList = n.getCommentList();
                    for (Comment c : commentList) {
                        count += c.getSubCommentList().size();
                    }
                    count += commentList.size();
                    return NewsResponse.builder()
                            .id(n.getId())
                            .banner(n.getBanner())
                            .content(n.getContent())
                            .title(n.getTitle())
                            .hashTag(n.getHashTag())
                            .lastUpdateTime(DateTimeConvert.convertLongToDate(n.getLastUpdateTime()))
                            .commentNum(count)
                            .build();
                })
                .collect(Collectors.toList());
        return new NewsSearchResponse(collect, hashTag);
    }

    public NewsResponse getNewsById(String id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND + id);
        }
        News news = optionalNews.get();
        int sum = news.getCommentList()
                .stream()
                .mapToInt(comment -> comment.getSubCommentList().size())
                .sum();

        return NewsResponse.builder()
                .id(news.getId())
                .banner(news.getBanner())
                .hashTag(news.getHashTag())
                .content(news.getContent())
                .title(news.getTitle())
                .lastUpdateTime(DateTimeConvert.convertLongToDate(news.getLastUpdateTime()))
                .commentNum(sum)
                .build();
    }

    @Override
    public NewsResponse insert(String token, CreateNewsRequest createNewsRequest) {
        Role role = Role.valueOf(jwtService.parseTokenToRole(token));
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        News news = new News();
        news.setCreateUserId(jwtService.parseTokenToId(token));
        news.setBanner(createNewsRequest.getBanner());
        news.setTitle(createNewsRequest.getTitle());
        news.setContent(createNewsRequest.getContent());
        news.setHashTag(createNewsRequest.getHashTag());
        news.setCreateTime(System.currentTimeMillis());
        news.setLastUpdateTime(System.currentTimeMillis());
        News insertedNews = newsRepository.insert(news);
        return NewsConverter.convertToNewsResponse(insertedNews, 0);
    }

    @Override
    public NewsResponse save(String id, String token, UpdateNewsRequest updateNewsRequest) {
        Optional<News> optionalNews = newsRepository.findById(id);

        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND + id);
        }
        News newsToUpdate = optionalNews.get();

        JwtData jwtData = jwtService.parseToken(token);
        Role role = jwtData.getRole();
        String updateUserId = jwtData.getId();

        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }

//      id, createUserId, createTime, comment no need to change
        // fields that can update if not null
        if (!updateNewsRequest.getBanner().isEmpty()) {
            newsToUpdate.setBanner(updateNewsRequest.getBanner());
        }
        if (!updateNewsRequest.getTitle().isEmpty()) {
            newsToUpdate.setTitle(updateNewsRequest.getTitle());
        }
        if (!updateNewsRequest.getContent().isEmpty()) {
            newsToUpdate.setContent(updateNewsRequest.getContent());
        }
        if (!updateNewsRequest.getHashTag().isEmpty()) {
            newsToUpdate.setHashTag(updateNewsRequest.getHashTag());
        }
        // fields that must update
        newsToUpdate.setUpdateUserId(updateUserId);
        newsToUpdate.setLastUpdateTime(System.currentTimeMillis());
        News updatedNews = newsRepository.save(newsToUpdate);
        int sum = updatedNews.getCommentList()
                .stream()
                .mapToInt(comment -> comment.getSubCommentList().size())
                .sum();

        return NewsConverter.convertToNewsResponse(updatedNews, sum);
    }

    public void deleteNewsById(String id, String token) {
        String deleteUserId = jwtService.parseTokenToId(token);

        Optional<News> newsToDelete = newsRepository.findById(id);
        if (newsToDelete.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND + id);
        }
        News news = newsToDelete.get();

        String createUserIdOfNews = news.getCreateUserId();

        if (!deleteUserId.equals(createUserIdOfNews)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }

        commentRepository.deleteAllByContainerId(id);
        subCommentRepository.deleteAllByContainerId(id);
        newsRepository.deleteById(id);

    }

    @Override
    public String addCommentToNews(String newsId, String token, CreateCommentRequest createCommentRequest) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND);
        }

        News news = optionalNews.get();

        String userId = jwtService.parseTokenToId(token);

        Comment comment = new Comment();
        comment.setContent(createCommentRequest.getContent());
        comment.setContainerId(newsId);
        comment.setCreateCommentTime(System.currentTimeMillis());
        comment.setUserId(userId);
        Comment result = commentRepository.save(comment);
        news.getCommentList().add(comment);
        newsRepository.save(news);
        return result.getId();
    }

    @Override
    public void deleteCommentById(String commentId, String token) {

        JwtData jwtData = jwtService.parseToken(token);
        Role role = jwtData.getRole();
        String userDeleteId = jwtData.getId();

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_OBJECT_NOT_FOUND + commentId);
        }
        Comment commentToDelete = commentOptional.get();

        if (!userDeleteId.equals(commentToDelete.getUserId())) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        if (!role.equals(Role.ADMIN)) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }

        String newsId = commentToDelete.getContainerId();
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND + newsId);
        }
        News news = optionalNews.get();

        List<Comment> commentList = news.getCommentList();
        for (Comment c : commentList) {
            if (c.getId().equals(commentToDelete.getId())) {
                commentList.remove(commentToDelete);
            }
        }

        subCommentRepository.deleteAllByCommentId(commentId);
        commentRepository.delete(commentToDelete);
        newsRepository.save(news);

    }

    @Override
    public String addSubCommentToNews(String commentId, String token, CreateSubCommentRequest createSubCommentRequest) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_COMMENT_NOT_FOUND + commentId);
        }
        Comment comment = optionalComment.get();

        JwtData jwtData = jwtService.parseToken(token);
        String userId = jwtData.getId();

        Optional<News> optionalNews = newsRepository.findById(comment.getContainerId());
        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND);
        }
        News news = optionalNews.get();

        SubComment subComment = SubCommentConverter.convertToInsert(commentId, news.getId(), userId, createSubCommentRequest);

        comment.getSubCommentList().add(subComment);
        SubComment result = subCommentRepository.insert(subComment);
        commentRepository.save(comment);

        List<Comment> commentList = news.getCommentList();
        for (Comment c : commentList) {
            if (c.getId().equals(commentId)) {
                c.getSubCommentList().add(subComment);
            }
        }

        newsRepository.save(news);

        return result.getId();
    }

    @Override
    public void deleteSubCommentById(String subCommentId, String token) {
        JwtData jwtData = jwtService.parseToken(token);
        String userDeleteId = jwtData.getId();
        Role role = jwtData.getRole();

        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        if (subCommentOptional.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_OBJECT_NOT_FOUND + subCommentId);
        }
        SubComment subCommentToDelete = subCommentOptional.get();

        Comment comment = commentRepository.findCommentById(subCommentToDelete.getCommentId());

        String userCreateCommentId = comment.getUserId();

        if ((!userDeleteId.equals(subCommentToDelete.getUserId())) || (!role.equals(Role.ADMIN)) || !(userCreateCommentId.equals(userDeleteId))) {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED);
        }
        subCommentRepository.delete(subCommentToDelete);
    }

    @Override
    public List<CommentResponse> getListComment(String newsId, String token, int skip, int take) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_NEWS_NOT_FOUND + newsId);
        }
        JwtData jwtData = jwtService.parseToken(token);
        String userGetRole = jwtData.getRole().toString();
        if (userGetRole.equals(Role.USER) || userGetRole.equals(Role.ADMIN.toString())) {
            News news = optionalNews.get();
            return news.getCommentList()
                    .stream()
                    .map(comment -> {
                        User user = userRepository.findUserById(comment.getUserId());
                        return CommentResponse.builder()
                                .id(comment.getId())
                                .avatar(user.getAvatarId())
                                .userName(user.getName())
                                .content(comment.getContent())
                                .createTime(DateTimeConvert.convertLongToDate(comment.getCreateCommentTime()))
                                .subCommentNumber(comment.getSubCommentList().size())
                                .timeReply(DateTimeConvert.takeTimeReply(comment.getCreateCommentTime()))
                                .build();
                    })
                    .skip((long) skip * take)
                    .limit(take)
                    .collect(Collectors.toList());
        }else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_GET_OBJECT);
        }
    }

    @Override
    public List<SubCommentResponse> getListSubComment(String commentId, String token, int skip, int take) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CustomException(ResponseObject.STATUS_CODE_NOT_FOUND, ResponseObject.MESSAGE_COMMENT_NOT_FOUND + commentId);
        }
        JwtData jwtData = jwtService.parseToken(token);
        String userGetRole = jwtData.getRole().toString();
        if (userGetRole.equals(Role.USER) || userGetRole.equals(Role.ADMIN.toString())) {
            Comment comment = optionalComment.get();
            return comment.getSubCommentList()
                    .stream()
                    .map(subComment -> {
                        User user = userRepository.findUserById(comment.getUserId());
                        return SubCommentResponse.builder()
                                .id(subComment.getId())
                                .avatar(user.getAvatarId())
                                .userName(user.getName())
                                .content(subComment.getContent())
                                .createTime(DateTimeConvert.convertLongToDate(subComment.getCreateCommentTime()))
                                .timeReply(DateTimeConvert.takeTimeReply(subComment.getCreateCommentTime()))
                                .build();
                    })
                    .skip((long) skip * take)
                    .limit(take)
                    .collect(Collectors.toList());
        } else {
            throw new CustomException(ResponseObject.STATUS_CODE_UNAUTHORIZED, ResponseObject.MESSAGE_UNAUTHORIZED_TO_GET_OBJECT);
        }
    }

}
