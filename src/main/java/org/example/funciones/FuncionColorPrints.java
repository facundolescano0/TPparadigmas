package org.example.funciones;

import org.example.model.Colores;
import org.fusesource.jansi.Ansi;
import java.lang.reflect.Field;

    public class FuncionColorPrints {
        public Ansi obtenerColorANSI(Colores.Color color) {
            try {
                Field field = Ansi.Color.class.getDeclaredField(color.name());
                Ansi.Color ansiColor = (Ansi.Color) field.get(null);
                return Ansi.ansi().fg(ansiColor);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return Ansi.ansi();
            }
        }
    }