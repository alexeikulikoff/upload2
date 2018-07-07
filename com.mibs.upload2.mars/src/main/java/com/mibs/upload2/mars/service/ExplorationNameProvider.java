package com.mibs.upload2.mars.service;

import com.mibs.upload2.mars.utils.MUtils;

public interface ExplorationNameProvider {
	 default  String ExplorationName() {
		return MUtils.UniqueID();
	}
}
