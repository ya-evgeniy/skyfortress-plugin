package ru.jekarus.skyfortress.v3.lang;

import jekarus.hocon.config.serializer.annotation.Generics;

import java.util.List;

public class SfTeamLanguage {

    @Generics(String.class)
    public List<String> names;

}
