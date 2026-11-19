package com.studygenai;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestClient;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController @RequestMapping("/api") @CrossOrigin(origins={"http://localhost:5173","http://127.0.0.1:5173"})
public class StudyController {
  private final RestClient http = RestClient.builder().build();
  private final ObjectMapper json = new ObjectMapper();
  private final String apiKey;
  private final String model;
  private final StudyPackRepository packs;
  public StudyController(@org.springframework.beans.factory.annotation.Value("${gemini.api-key:}") String apiKey, @org.springframework.beans.factory.annotation.Value("${gemini.model:gemini-3.1-flash-lite}") String model, StudyPackRepository packs){this.apiKey=apiKey;this.model=model;this.packs=packs;}
  @GetMapping("/health") public Map<String,String> health(){ return Map.of("status","ok","service","StudyGenAI API"); }
  @GetMapping("/dashboard") public Map<String,Object> dashboard(){ return Map.of("streak",7,"hours",24,"topics",18,"accuracy",86,"recent",List.of("JavaScript Fundamentals","Operating Systems","Machine Learning")); }
  @PostMapping("/generate") public Map<String,Object> generate(@RequestBody Map<String,String> body){ String type=body.getOrDefault("type","notes"); String topic=body.getOrDefault("topic","your topic"); String prompt="You are StudyGenAI, an expert tutor. Create a concise but useful "+type+" study pack for: "+topic+". Return exactly four numbered key points, each one sentence, with no markdown."; String answer=callGemini(prompt); List<String> items=new ArrayList<>(); for(String line:answer.split("\\r?\\n")){String clean=line.replaceFirst("^\\s*\\d+[.)]\\s*","").trim(); if(!clean.isBlank()) items.add(clean);} if(items.isEmpty()) items=List.of("Core concepts and definitions","Practical examples with clear explanations","Quick revision checklist","Exam-focused questions to test understanding"); String provider=apiKey.isBlank()?"local-fallback":"gemini"; List<String> savedItems=items.subList(0,Math.min(items.size(),6)); packs.save(new StudyPack(topic,type,provider,String.join("\\n",savedItems))); return Map.of("type",type,"topic",topic,"title",topic+" — AI Study Pack","content",savedItems,"createdAt",new Date().toString(),"provider",provider); }
  private String callGemini(String prompt){ if(apiKey.isBlank()) return "1. Core concepts and definitions\n2. Practical examples with clear explanations\n3. Quick revision checklist\n4. Exam-focused questions to test understanding"; try { Map<String,Object> part=Map.of("text",prompt); Map<String,Object> content=Map.of("parts",List.of(part)); Map<String,Object> request=Map.of("contents",List.of(content)); String raw=http.post().uri("https://generativelanguage.googleapis.com/v1beta/models/"+model+":generateContent?key="+apiKey).contentType(MediaType.APPLICATION_JSON).body(request).retrieve().body(String.class); JsonNode root=json.readTree(raw); return root.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText(""); } catch(Exception ignored){ return "1. Core concepts and definitions\n2. Practical examples with clear explanations\n3. Quick revision checklist\n4. Exam-focused questions to test understanding"; } }
}
