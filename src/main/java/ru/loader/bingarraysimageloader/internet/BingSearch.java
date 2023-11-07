package ru.loader.bingarraysimageloader.internet;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BingSearch {
    public BingSearch(){

    }

    public List<String> getImageLinks(String request, int howMany) throws IOException {
        List<String> imageLinks = new ArrayList<>();
        howMany = controlValue(howMany);
        int step = howMany / 10;
        for(int i=1; i<=howMany; i+=step){
            String fullPath = request + i;
            List<String> parse = parse(fullPath);
            Map<String, String> map = new HashMap<>();
            for(String parseLine : parse){
                if(parseLine.contains("Результат")){
                    resultParse(parseLine,map);
                }
            }
            int j=0;
            for(String oip : map.keySet()){
                imageLinks.addAll(finalCut(map.get(oip),oip));
                if(j<step){
                    j++;
                }else{
                    break;
                }
            }
        }
        return imageLinks;
    }

    private void resultParse(String line, Map<String,String> map) throws IOException {
        String oip = null;
        String id = null;
        String regex = "id=.+?&";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String result = line.substring(matcher.start(), matcher.end()-1);
            if(result.contains("id=OIP.")){
                oip = result.substring(3);
            }else if(result.length() == 43){
                id = result.substring(3);
            }
        }
        map.put(oip,id);
    }

    private List<String> finalCut(String id, String oip) throws IOException {
        String link = "";
        List<String> finalLink = new ArrayList<>();
        String imageLink = "https://www.bing.com/images/search?view=detailV2&ccid=zOTi41sc&id=" + id + "&thid=" + oip;
        List<String> parse = parse(imageLink);
        for(String newLine : parse){
            String regex = "https://tse2.mm.bing.net/th/id/OIP.+?\\*";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(newLine);
            while (matcher.find()){
                link = newLine.substring(matcher.start(), matcher.end() -1);
                System.out.println(link);
            }
        }
        finalLink.add(link);
        return finalLink;
    }

    private List<String> parse(String urlAddress) throws IOException {
        List<String> lines = new ArrayList<>();
        URL url = new URL(urlAddress);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }

    private int controlValue(int num){
        return num < 10 ? 10 : (Math.min(num, 101));
    }

    private void save(){
        /*URL url = new URL(line);
        InputStream inputStream = url.openStream();
        File file = new File("D:/img/" + line.substring(line.length()/2, line.length()/2+10) + ".jpg");
        if(!file.exists()) {
            System.out.println("save: " + file.toPath());
            Files.copy(inputStream, file.toPath());
            TOUSTED
        }*/
    }
}
