package visidia.stats;

import java.util.EventListener;

public interface StatListener extends EventListener {

	void updatedStats(Statistics stats);
	
}
