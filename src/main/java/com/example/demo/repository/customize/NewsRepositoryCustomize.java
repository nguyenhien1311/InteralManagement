package com.example.demo.repository.customize;

import com.example.demo.domain.News;

import java.util.List;

public interface NewsRepositoryCustomize {
    List<News> findAllByHashTag(String hashTag,int skip,int limit);

}
