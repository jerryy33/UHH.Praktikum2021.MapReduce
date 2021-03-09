import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
* Die Klasse holt relavante Informationen aus einem Tweet, um ihn dann für das Map-Reduce zu nutzen.
* */
public class Parser {


    // Eine Liste in der die Englischen stopwords gespeichert werden
    private List<String> stopwords_eng;

    //Eine Liste in der die Deutschen stopwords gespeichert werden
    private List<String> stopwords_ger;

    //Eine Liste in der die Spanische stopwords gespeichert werden
    private List<String> stopwords_es;


    public String getDate(JSONObject tweet) throws ParseException
    {
        String date = (String) tweet.get("created_at");
        String [] einheiten = date.split("\\s+");

        return einheiten[1] + einheiten[2] +einheiten[5];
    }


    public String getLanguage(JSONObject tweet) throws ParseException
    {
        return (String) tweet.get("lang");
    }

    /*
    * Extrahiert die Hashtags aus dem JSON Object.
    * @param tweet Ein kompletter Tweet als JSONObject.
    * @return hashtagList Liste die alle Hashtags beinhaltet.
    */
    public LinkedList<String> getHashtags (JSONObject tweet) throws ParseException
    {
        LinkedList<String> hashtagsList = new LinkedList<>();

        JSONObject ent = (JSONObject) tweet.get("entities");
        JSONArray hash = (JSONArray) ent.get("hashtags");

        if (!hash.isEmpty())
        {
            for (Object o : hash)
            {
                JSONObject text = (JSONObject) o;
                if (text.containsKey("text"))
                {
                    hashtagsList.add(text.get("text").toString());
                }
            }
        }
        return hashtagsList;
    }

    /* Extrahiert den Text, der in einem Tweet vorkommt aus einem JSONObject und tut jedes Wort in eine Liste.
     * Dabei wird der Text "gecleant".
     * @param tweet Ein JSONObject welches ein Tweet beinhaltet.
     * @return textList Eine Liste die alle Wörter beinhaltet.
     */
    public LinkedList<String> getText(JSONObject tweet) throws ParseException
    {
        LinkedList<String> textList = new LinkedList<>();

        String t = (String) tweet.get("text");
        String cleanText = cleanText(t.toLowerCase(Locale.ROOT));
        StringTokenizer itr = new StringTokenizer(cleanText);
        while (itr.hasMoreTokens())
        {
            textList.add(itr.nextToken());
        }
        return textList;
    }
    /*
     * Hilfsmethode die den Text von nicht inhaltlich wichtigen Wörtern befreit.
     * @param t Der Text als String der gecleant werden soll
     * @return s.replaceAll String der keine Hashtags und stopwords enthält.
     */
    private String cleanText(String t) {
        String s = removeAllStopWords(t.toLowerCase(Locale.ROOT));
        String r = s.replaceAll("[\\S]+://[\\S]+", "");
        return r.replaceAll("#[^\\s]+", "");
    }

    /*
     * Entfernt alle sogenannten stopwords aus unserem Twitter-Textfeld. Berücksichtigte Sprachen(Deutsch, Englisch).
     * @param t Ein String der den Text enthält.
     * @return String Der Text als String allerdings ohne die stopwords.
     */
    public String removeAllStopWords(String t)
    {
        makeStopwordLists();
        ArrayList<String> allWords = Stream.of(t.split(" ")).collect(Collectors.toCollection
                (ArrayList<String>::new));

        stopwords_eng.removeAll(Collections.singleton(null));
        allWords.removeAll(stopwords_eng);

        stopwords_es.removeAll(Collections.singleton(null));
        allWords.removeAll(stopwords_es);

        stopwords_ger.removeAll(Collections.singleton(null));
        allWords.removeAll(stopwords_ger);
        return allWords.stream().collect(Collectors.joining(" "));
    }

    /*
     * Liest die stopwords Files ein und speichert sie in einer Liste.
    */
    private void makeStopwordLists()
    {
        String path_eng = "/english_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_eng)) {
            stopwords_eng = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path_ger = "/german_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_ger)) {
            stopwords_ger = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path_es = "/spanish_stopwords";
        try (InputStream resource = Parser.class.getResourceAsStream(path_es)) {
            stopwords_es = new BufferedReader(new InputStreamReader(resource,
                    StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}