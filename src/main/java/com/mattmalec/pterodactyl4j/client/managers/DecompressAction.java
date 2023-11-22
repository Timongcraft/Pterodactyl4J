package com.mattmalec.pterodactyl4j.client.managers;

import com.mattmalec.pterodactyl4j.PteroAction;
import com.mattmalec.pterodactyl4j.client.entities.Directory;

public interface DecompressAction extends PteroAction<Void> {
    DecompressAction setRoot(Directory rootDirectory);
}
