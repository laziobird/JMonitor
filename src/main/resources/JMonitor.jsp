<%@page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="monitor.JMonitor,net.sf.json.JSONObject,java.util.*"%>
<%
	JSONObject result = new JSONObject();
	Map<String,Long> tmp = JMonitor.getRealResult();
	long ResourceCount = 0;
	for(String key:tmp.keySet()){
		Long value = JMonitor.getRealResult().get(key);
		if(key.indexOf("Resource_TCount")>=0) ResourceCount += value;		
		result.put(key, value);		
	}	
	result.put("All_Resource_TCount", ResourceCount);	
/* 	for(String key:tmp.keySet()){
		if(key.indexOf("Resource_TCount")>=0){
			Long value = JMonitor.getRealResult().get(key);			
		}

	} */		
	out.append(result.toString());	
%>