package com.mballem.demoajax.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mballem.demoajax.domain.SocialMetaTag;

@Service
public class SocialMetaTagService {

	private static Logger log = LoggerFactory.getLogger(SocialMetaTagService.class);
	
	public SocialMetaTag getSocialMetaTagByUrl(String url) {
		
		SocialMetaTag twitter = getTwitterCardByUrl(url);
		
		if(!isEmpty(twitter)) {
			return twitter;
		}
		
		SocialMetaTag openGraph = getOpenGraphByUrl(url);
		if(!isEmpty(openGraph)) {
			return openGraph;
		}
		
		return null;
	}
	
	
	/**
	 * TWITTER
	 * @param url
	 * @return
	 */
	private SocialMetaTag getTwitterCardByUrl(String url) {
		SocialMetaTag tag = new SocialMetaTag();
		
		try {
			
			Document doc = Jsoup.connect(url)	
					.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.get();
			
			
			
			tag.setTitle(doc.head().select("meta[name=twitter:title]").attr("content"));
			tag.setSite(doc.head().select("meta[name=twitter:site]").attr("content"));
			tag.setImage(doc.head().select("meta[name=twitter:image]").attr("content"));
			tag.setUrl(doc.head().select("meta[name=twitter:url]").attr("content"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e.getCause());
		}
		
		return tag;
	}
	
	
	/**
	 * OPENGRAPH
	 * @param url
	 * @return
	 */
	private SocialMetaTag getOpenGraphByUrl(String url) {
		SocialMetaTag tag = new SocialMetaTag();
		
		try {
			
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.get();
			tag.setTitle(doc.head().select("meta[property=og:title]").attr("content"));
			tag.setSite(doc.head().select("meta[property=og:site_name]").attr("content"));
			tag.setImage(doc.head().select("meta[property=og:image]").attr("content"));
			tag.setUrl(doc.head().select("meta[property=og:url]").attr("content"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e.getCause());
		}
		
		return tag;
	}
	
	
	private boolean isEmpty(SocialMetaTag tag) {
		
		if(tag.getImage().isEmpty()) return true;
		if(tag.getSite().isEmpty()) return true;
		if(tag.getTitle().isEmpty()) return true;
		if(tag.getUrl().isEmpty()) return true;
		
		return false;
	}
	
}
