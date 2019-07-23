package br.com.estoque.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;


public class BuscaCep {

    private Object data;

    public HashMap<String , Object > retornaEndereco(String cep) throws UnirestException, IllegalAccessException {

        HashMap<String , Object > endereco = new HashMap<>();

        try {
            try {
                HttpResponse<JsonNode> jsonResponse = Unirest.get("https://viacep.com.br/ws/"+cep+"/json/unicode/")
                        .header("accept", "application/json")
                        .asJson();

                JsonNode responseBody = jsonResponse.getBody();
                JSONArray arr = responseBody.getArray();

                endereco.put("estado" , arr.getJSONObject(0).getString("uf"));
                endereco.put("cidade" , arr.getJSONObject(0).getString("localidade"));
                endereco.put("logradouro" , arr.getJSONObject(0).getString("logradouro"));
                endereco.put("bairro" , arr.getJSONObject(0).getString("bairro"));


            }catch (UnirestException e) {

            }


        } catch (JSONException e) {
            endereco = null;
        }


        return endereco;

    }
}
