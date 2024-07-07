package com.bringco.test.controller;

import com.bringco.test.response.BasicResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "api", description = "API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

        @GetMapping("parsing")
        @Operation(summary = "단어 빈도수 체크 api", description = "url내 단어 빈도수 체크")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Success",
                        content = {@Content(schema = @Schema(implementation = BasicResponseDto.class))}),
                @ApiResponse(responseCode = "404", description = "Not Found"),
        })
        public BasicResponseDto exampleAPI(
                @Parameter(name = "url", description = "url", required = true)
                @RequestParam String  url
        ) {

            try {

                String s = String.format("PathValue = %d , ParamValue = %s, Request Email : %s", 1, "dddd", "dddd");
                Document doc = Jsoup.connect(url).get();

                String content = Jsoup.clean(doc.html(), Safelist.none());

                Map<String, Integer> wordFrequency = new HashMap<>();

                for (String word : content.replaceAll("&nbsp", "").split("\\s+")) {
                    if (!word.isEmpty()) {
                        wordFrequency.merge(word, 1, Integer::sum);
                    }
                }

                List<Map.Entry<String, Integer>> entryList = new LinkedList<>(wordFrequency.entrySet());
                entryList.sort(((o1, o2) -> wordFrequency.get(o2.getKey()) - wordFrequency.get(o1.getKey())));

                Map<String, Integer> result = new LinkedHashMap<>();
                for(Map.Entry<String, Integer> entry : entryList){
                    System.out.println("key : " + entry.getKey() + ", value : " + entry.getValue());

                    result.put(entry.getKey(), entry.getValue());
                }
                return new BasicResponseDto(true, "Success",  result);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }





        }
}
