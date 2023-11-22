/*
 *    Copyright 2021-2022 Matt Malec, and the Pterodactyl4J contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mattmalec.pterodactyl4j.client.entities.impl;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.client.entities.Directory;
import com.mattmalec.pterodactyl4j.client.entities.File;
import com.mattmalec.pterodactyl4j.client.managers.DecompressAction;
import com.mattmalec.pterodactyl4j.exceptions.IllegalActionException;
import com.mattmalec.pterodactyl4j.requests.PteroActionImpl;
import com.mattmalec.pterodactyl4j.requests.Route;
import com.mattmalec.pterodactyl4j.utils.Checks;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class DecompressActionImpl extends PteroActionImpl<Void> implements DecompressAction {

	private final File compressedFile;

	private Directory rootDirectory;

	public DecompressActionImpl(ClientServer server, File compressedFile, PteroClientImpl impl) {
		super(impl.getP4J(), Route.Files.DECOMPRESS_FILE.compile(server.getIdentifier()));
		this.compressedFile = compressedFile;
	}

	@Override
	public DecompressAction setRoot(Directory rootDirectory) {
		this.rootDirectory = rootDirectory;
		return this;
	}

	@Override
	protected RequestBody finalizeData() {
		Checks.notNull(compressedFile, "Compressed File");

		if(rootDirectory == null) {
			JSONObject json = new JSONObject().put("root", "/").put("file", compressedFile.getPath());
			return getRequestBody(json);
		}
		String rootPath = rootDirectory.getPath() + "/";
		String filePath = compressedFile.getPath();
		if(!filePath.startsWith(rootPath)) {
			throw new IllegalActionException("compressed file isn't within the given root directory");
		}
		JSONObject json = new JSONObject().put("root", rootDirectory.getPath()).put("file", filePath.substring(rootPath.length()));
		return getRequestBody(json);
	}

}
