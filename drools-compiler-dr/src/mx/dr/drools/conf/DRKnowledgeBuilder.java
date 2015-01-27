package mx.dr.drools.conf;
/*
*
*
* Copyright (C) 2011-2012 Jorge Luis Martinez Ramirez
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
* Author: Jorge Luis Martinez Ramirez
* Email: jorgemfk1@gmail.com
*/
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.builder.impl.KnowledgeBuilderImpl;
import org.drools.compiler.Dialect;
import org.drools.io.ResourceFactory;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

public class DRKnowledgeBuilder {
    public static void assetsToFiles(Context context, String assetfolder, String ext) throws IOException{
    	AssetManager assetManager = context.getAssets(); 
    	InputStream io;
    	FileOutputStream  fOut;
        byte[] buff = new byte[1024];
        int bytesRead = 0;
		for (String fname :assetManager.list(assetfolder)){
			if (fname.endsWith(ext)){
			  io=assetManager.open(assetfolder+"/"+fname);
			  fOut = context.openFileOutput(fname,Context.MODE_WORLD_READABLE);
	          while((bytesRead = io.read(buff)) != -1) {
                fOut.write(buff,0, bytesRead);
                
	          }
	          fOut.close();
	          io.close();
			}
		}
    }
	public static KnowledgeBase loadKnowledge(final Context context, String propspath, String knowlegpath, ResourceType type){
		String pakage=context.getPackageName();
		Properties props = new Properties();
		try {
			props.load(Dialect.class.getResourceAsStream(propspath));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		for (String fname: context.getFilesDir().list()){
			if (fname.endsWith("dex")||fname.endsWith("apk")){
				context.deleteFile(fname);
			}
		}
		KnowledgeBuilderConfiguration kbuilderConf = 
				KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration(props, Dialect.class.getClassLoader() );
		

		System.setProperty("java.version", "1.5");
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbuilderConf);
		((KnowledgeBuilderImpl)kbuilder).setPackage(pakage);
		
		kbuilder.add(ResourceFactory.newClassPathResource(knowlegpath), type);
		if( kbuilder.hasErrors() ) {
			if (context instanceof Activity) {
				((Activity)context).runOnUiThread(new Runnable() {
					  public void run() {
						  Toast.makeText(context,
									kbuilder.getErrors().toString(), Toast.LENGTH_SHORT).show();
					  }
					});	
			}else{
				System.err.println(kbuilder.getErrors().toString());
			}
			
		    return null;
		}
		KnowledgeBaseConfiguration kbaseConf =
                KnowledgeBaseFactory.newKnowledgeBaseConfiguration( null, ClassLoader.getSystemClassLoader() );
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase( kbaseConf );
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;

	}
}
