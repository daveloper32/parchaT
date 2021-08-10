package com.developers.parchat.model.entity;

public class BusquedaSeleccionarActividad {
    private boolean areasVerdesSelected;
    private boolean arteSelected;
    private boolean cineSelected;
    private boolean musicaSelected;
    private boolean restaurantesSelected;
    private boolean sorprendemeSelected;

    public BusquedaSeleccionarActividad(boolean areasVerdesSelected, boolean arteSelected, boolean cineSelected, boolean musicaSelected, boolean restaurantesSelected, boolean sorprendemeSelected) {
        this.areasVerdesSelected = areasVerdesSelected;
        this.arteSelected = arteSelected;
        this.cineSelected = cineSelected;
        this.musicaSelected = musicaSelected;
        this.restaurantesSelected = restaurantesSelected;
        this.sorprendemeSelected = sorprendemeSelected;
    }

    public boolean isAreasVerdesSelected() {
        return areasVerdesSelected;
    }

    public boolean isArteSelected() {
        return arteSelected;
    }

    public boolean isCineSelected() {
        return cineSelected;
    }

    public boolean isMusicaSelected() {
        return musicaSelected;
    }

    public boolean isRestaurantesSelected() {
        return restaurantesSelected;
    }

    public boolean isSorprendemeSelected() {
        return sorprendemeSelected;
    }
}
