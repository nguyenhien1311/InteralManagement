package com.example.demo.response.news;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsSearchResponse {
	private List<NewsResponse> news;
	private String hashTags;
}
