package com.yibo.yiboapp.entify;

import java.util.HashMap;
import java.util.Map;

public class GameKey {
	
	static Map<String,SportType> sportMap = new HashMap<>();
	static{
		sportMap.put("FT", SportType.FOOTBALL);
		sportMap.put("BK", SportType.BASKETBALL);
	}
	
	static Map<String,GameTimeType> gameTimeMap = new HashMap<>();
	
	static{
		gameTimeMap.put("FT", GameTimeType.FUTURE);
		gameTimeMap.put("TD", GameTimeType.TODAY);
		gameTimeMap.put("RB", GameTimeType.RUNBALL);
	}
	
	static Map<String,DataType> dataTypeMap = new HashMap<>();
	
	static{
		dataTypeMap.put("MN", DataType.MAIN);
		dataTypeMap.put("BC", DataType.BALL_COUNT);
		dataTypeMap.put("HF", DataType.FULL_AND_HALF);
		dataTypeMap.put("MX", DataType.MIX);
		dataTypeMap.put("HTI", DataType.TIME_HALF);
		dataTypeMap.put("TI", DataType.TIME_FULL);
		dataTypeMap.put("CP", DataType.CHAMPION);
	}
	
	public GameKey(String gameKey){
		this.gameKey = gameKey;
		gameKeys = gameKey.split("_");
		if(gameKeys.length != 3){
			throw new IllegalStateException("非法请求");
		}
		this.sportType = sportMap.get(gameKeys[0]);
		this.gameTimeType = gameTimeMap.get(gameKeys[1]);
		this.dataType = dataTypeMap.get(gameKeys[2]);
	
	}
	
	public void checkData(){
		if(this.sportType == null || this.gameTimeType == null || this.dataType == null){
			throw new IllegalStateException("非法请求");
		}
	}
	
	private GameTimeType gameTimeType;
	
	private SportType sportType;
	
	private DataType dataType;
	
	private String gameKey;
	
	private String [] gameKeys;
	
	public boolean isFootball(){
		return this.sportType == SportType.FOOTBALL;
	}
	
	public boolean isBasketball(){
		return this.sportType == SportType.BASKETBALL;
	}
	
	public boolean isRunball(){
		return this.gameTimeType == GameTimeType.RUNBALL;
	}
	
	public boolean isToday(){
		return this.gameTimeType == GameTimeType.TODAY;
	}

	public boolean isFuture(){
		return this.gameTimeType == GameTimeType.FUTURE;
	}
	
	public boolean isMain(){
		return this.dataType == DataType.MAIN;
	}
	
	public boolean isMix(){
		return this.dataType == DataType.MIX;
	}
	
	public boolean isBallCount(){
		return this.dataType == DataType.BALL_COUNT;
	}
	
	public boolean isHalfTime(){
		return this.dataType == DataType.TIME_HALF;
	}
	
	public boolean isFullTime(){
		return this.dataType == DataType.TIME_FULL;
	}
	
	public boolean isTime(){
		return this.isHalfTime() || this.isFullTime();
	}
	
	public boolean isChampion(){
		return this.dataType == DataType.CHAMPION;
	}
	
	public GameTimeType getGameTimeType() {
		return gameTimeType;
	}

	public void setGameTimeType(GameTimeType gameTimeType) {
		this.gameTimeType = gameTimeType;
	}

	public SportType getSportType() {
		return sportType;
	}

	public void setSportType(SportType sportType) {
		this.sportType = sportType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getGameKey() {
		return gameKey;
	}

	public void setGameKey(String gameKey) {
		this.gameKey = gameKey;
	}
}
