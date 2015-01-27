package mx.dr.drools.builder;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.android.dx.dex.file.DexFile;

public enum KnowledgeLibBuilder {
     INSTANCE;
     private int targetVersion=8;
     private String pakage;
     private String filepath;

	public void setPakage(String pakage) {
		this.pakage = pakage;
		this.filepath="/data/data/"+pakage+"/files/";
	}

	public String getFilePath() {
		return filepath;
	}

	public int getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(int targetVersion) {
		this.targetVersion = targetVersion;
	}
	
	public void buildApk(File file, DexFile dex) throws IOException{

		ZipOutputStream zos;

		zos=new ZipOutputStream(new FileOutputStream(file));
		ZipEntry entry=new ZipEntry("classes.dex");
		zos.putNextEntry(entry);
		dex.writeTo(zos, null, false);
		zos.closeEntry();
		zos.close();
	}
	
     
}
