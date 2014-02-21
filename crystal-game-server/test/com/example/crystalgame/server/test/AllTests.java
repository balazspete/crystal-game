package com.example.crystalgame.server.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.example.crystalgame.server.datawarehouse.DataStore;
import com.example.crystalgame.server.datawarehouse.ServerDataWarehouse;

@RunWith(Suite.class)
@SuiteClasses({ DataStore.class, ServerDataWarehouse.class })
public class AllTests {

}
