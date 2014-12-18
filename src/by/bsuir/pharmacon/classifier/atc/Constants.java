package by.bsuir.pharmacon.classifier.atc;

public interface Constants {
	
	public static final String URL = "http://www.rlsnet.ru";
	public static final String XPATH_FOR_LEFT_ATC_BUTTON = "//div[@class='my_menu_div']/a[contains(text(),'ÀÒÕ  êëàññèôèêàöèÿ') or (@id='atc_tree')]";
	public static final String XPATH_FOR_TOP_GROUP = "//*[@class='tree_fade']" ;
	public static final String SECOND_LEVEL_PATH = "//div[@class='subcatlist']//li/a";
	public static final String SECOND_LEVEL_PATHFOR_NAME_SEARCH = "//div[@class='subcatlist']//li/a[contains(text(),'NAME')]";
	public static final String CODE_PATH="//*[@id='page_head']";
	public static final String IMPACT_SUBSTANCE_VALUE_PATH="//*[@class='rest_nest']";
	public static final String spiderPattern = "ELEM <- code_CODE_ATC;;";
	public static final String firstLevelTemplate =  "GROUP_ATC => nrel_group_description: [DESCRIPTION] (*<- lang_ru;;*);;\n GROUP_ATC <- concept_ATC_group;; \n GROUP_ATC <= nrel_subdividing: { SUBDIV_LIST };;";
	public static final String secondLevelTemplate = "GROUP_ATC => nrel_group_description: [DESCRIPTION] (*<- lang_ru;;*);;\n	GROUP_ATC <- concept_ATC_group;;";
	public static final String codeTemplate = "code_CODE_ATC => nrel_code_description: [DESCRIPTION] (*<- lang_ru;;*);;\n code_CODE_ATC <- concept_ATC_code;;";
	public static final String groupAndCodeTemplate = "code_CODE_ATC => nrel_group_description: [DESCRIPTION] (*<- lang_ru;;*);; \nCODE_ATC <- concept_ATC_group;;\nCODE_ATC <- code_CODE_ATC;;\nCODE_ATC <= nrel_inclusion:{SUBDIV_LIST};;";
	public static final String rootFolderPath="d:\\PHARMACON1\\";
	public static final String FIRST = "firstLevel";
	public static final String SECOND = "secondLevel";
	public static final String THIRD = "thirdLevel";
	public static final String CODE = "code";
	public static final Integer START_ELEMENT = 0;
}
