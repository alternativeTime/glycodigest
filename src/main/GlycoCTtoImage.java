package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.Glycan;
import org.eurocarbdb.application.glycanbuilder.GlycanRenderer;
import org.eurocarbdb.application.glycanbuilder.GlycanRendererAWT;
import org.eurocarbdb.application.glycanbuilder.GlycoCTCondensedParser;
import org.eurocarbdb.application.glycanbuilder.MassOptions;
import org.eurocarbdb.application.glycanbuilder.SVGUtils;


public class GlycoCTtoImage {
	/*
	 * produce pictures from GlycoCT sequence
	 */
	String glycaninput;
	String name;
	public GlycoCTtoImage(String glycaninput, String name){
		this.glycaninput = glycaninput;
		this.name = name;
	}
       
	public String produceImage() throws IOException, Exception{
                //The workspace will initialise dictionaries and placement libraries for us
                BuilderWorkspace workspace=new BuilderWorkspace(new GlycanRendererAWT());
                workspace.setNotation("cfg"); //cfgbw | uoxf | uoxfcol | text
                
                //Get a reference to the renderer
                GlycanRenderer renderer=workspace.getGlycanRenderer();
                
                //Get an instance of the GlycoCT Parser
                GlycoCTCondensedParser parser=new GlycoCTCondensedParser(false);
                MassOptions options=MassOptions.empty();
         
                List<Glycan> glycanList=new ArrayList<Glycan>();

                //Parse in a GlycoCT condensed string from text and pass in empty MassOptions
	            Glycan glycan=parser.fromGlycoCTCondensed(glycaninput.trim(), options);
	                
	            //Generate a list of glycans to render

	            glycanList.add(glycan);
                
	            //Render glycans as a PNG
	            SVGUtils.export((GlycanRendererAWT) renderer, name, glycanList, true, true, "png");
                return name;
        }
        

}