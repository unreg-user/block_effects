package wta.entities;

import wta.entities.groups.projectiles.ProjectilesInit;

public class EntitiesInit {
    public static void init(){
        ProjectilesInit.init();
    }

    public static void initClient(){
        ProjectilesInit.initClient();
    }
}
