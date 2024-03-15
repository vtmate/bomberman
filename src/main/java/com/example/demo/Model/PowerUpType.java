package com.example.demo.Model;

public enum PowerUpType {
    //DEFAULT POWER UPS
    MOREBOMBS, //több bombája is lehet a játékosnak
    BIGGERRADIUS, //nagyobb a bomba hatótávja
    //BAD POWER UPS
    SNAIL, //lassabb a játékos
    SMALLERRADIUS, //kisebb a bomba hatótávja
    NOBOMBS, //nincs bombája a játékosnak
    IMMADIATEBOMB, //egyből lerakódik a bomba, amint a korábbi felrobbant
    //GOOD POWER UPS
    DETONATOR, //a bomba nem időre robban, aktiválható
    ROLLERSKATE, //gyorsabb a játékos
    SHIELD, //halhatatlan a játékos
    GHOST, //a játékos át tud menni a falakon
    GATE //doboz-hoz hasonló elemetket (3-at) tud lehelyezni a játékos
}