package nl.knmi.adaguc.tools;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.annotation.Resource;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.util.ConcurrentReferenceHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import nl.knmi.adaguc.tools.MyXMLParser.Options;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
public class MyXMLParserTest {


	@Rule
	public final ExpectedException exception = ExpectedException.none();

	
	@Test
	public void TestParseJSON () throws Exception {
		String json = Tools.readResource("json.json");
		Debug.println("json:\n" + json);
		
		/* Create json object from json string */
		JSONObject jsonObject = new JSONObject(json);
		
		/* Convert json to structure */
		MyXMLParser.XMLElement structureFromJSON = new MyXMLParser.XMLElement();
		structureFromJSON.parse(jsonObject);
		
		/* Convert structure to XMLstring */
		String xmlString = structureFromJSON.toString();
		Debug.println("xmlString:\n" + xmlString);
		
		/* Convert XMLstring to structure */
		MyXMLParser.XMLElement structureFromXML = new MyXMLParser.XMLElement();
		structureFromXML.parseString(xmlString);
		
		/* Convert structure to JSONString */
		String jsonString = structureFromXML.toJSON(Options.NONE);
		Debug.println("jsonString:\n" + jsonString);
		
		/* Create JSONObject from JSONString */
		JSONObject jsonObjectFromStructure = new JSONObject(jsonString);
		
		/* Check if this jsonObjectFromStructure is same as original json */
		final JSONObject obj1 = jsonObjectFromStructure;
		final JSONObject obj2 = jsonObject;
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode tree1 = mapper.readTree(obj1.toString());
		final JsonNode tree2 = mapper.readTree(obj2.toString());
		assertThat(tree1.equals(tree2), is(true));
	}


}
