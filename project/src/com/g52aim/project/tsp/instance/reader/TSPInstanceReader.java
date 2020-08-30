
package com.g52aim.project.tsp.instance.reader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import com.g52aim.project.tsp.instance.Location;
import com.g52aim.project.tsp.instance.TSPInstance;
import com.g52aim.project.tsp.interfaces.TSPInstanceInterface;
import com.g52aim.project.tsp.interfaces.TSPInstanceReaderInterface;

/**
 *
 * @author Warren G. Jackson
 *
 */
public class TSPInstanceReader implements TSPInstanceReaderInterface {

	@Override
	public TSPInstanceInterface readTSPInstance(Path path, Random random) {
		int cities = -1;
		Location[] locations = null;
		byte[] fileBytes;
		String ins = null;
		try {
			fileBytes = Files.readAllBytes(path);
			ins = new String(fileBytes);
		} catch (IOException e) {
			System.out.println("Error: Instance not found");
			e.printStackTrace();
		}

		String split = ins.split("DIMENSION :")[1];
		split = split.split("\\r?\\n")[0].trim();
		cities = Integer.parseInt(split);
		locations = new Location[cities];

		split = ins.split("NODE_COORD_SECTION")[1].trim();
		split = split.split("EOF")[0].trim().replace("\t", " ");
		String[] splits = split.split("\\r?\\n");

		for(int i = 0; i < splits.length; i++) {
			String[] insArr = splits[i].split(" ");
			locations[i] = new Location(Double.parseDouble(insArr[1]), Double.parseDouble(insArr[2]));
		}

		TSPInstance instance = new TSPInstance(cities, locations, random);
		return instance;
	}
}
