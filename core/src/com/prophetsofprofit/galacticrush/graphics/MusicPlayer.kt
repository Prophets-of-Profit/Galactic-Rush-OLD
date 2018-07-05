package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.Gdx

/**
 * A class which handles the playing of multiple tracks of music
 * Takes in a list of paths to the music
 * TODO: make sound manager
 */
class MusicPlayer(musicPath: Array<String> = arrayOf()) {

    //The list of music to be played by this music player
    private val music = MutableList(musicPath.size) { Gdx.audio.newMusic(Gdx.files.internal(musicPath[it])) }
    //The index of music playing
    var musicPlaying = 0
    //Getters and setters for the music volume
    var volume
        get() = this.music.map { it.volume }.average().toFloat()
        set(value) = this.music.forEach { it.volume = value }

    /**
     * Initializes; starts playing the first music in the list
     */
    init {
        this.start()
    }

    /**
     * Starts playing the music from the beginning
     */
    fun start() {
        if (music.isNotEmpty()) {
            music[0].play()
        }
    }

    /**
     * Adds a music to the MusicPlayer
     */
    fun addMusic(path: String) {
        val newMusic = Gdx.audio.newMusic(Gdx.files.internal(path))
        newMusic.volume = this.volume
        this.music.add(newMusic)
    }

    //TODO: make a removeMusic method?

    /**
     * Updates the music
     * If the currently playing track is no longer playing, move on to the next track
     * and play it
     */
    fun update() {
        if (this.music.isEmpty() || this.music[this.musicPlaying].isPlaying) {
            return
        }
        this.musicPlaying += 1
        this.musicPlaying %= music.size
        this.music[musicPlaying].play()
    }

    /**
     * Disposes of the music player by disposing of all of the contained music
     */
    fun dispose() {
        this.music.forEach { it.dispose() }
    }

}