package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.Gdx
import java.util.Random

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
    //Whether or not the music is shuffled
    private var shuffled = false
    //Whether or not the music player is running
    private var stopped = true
    //Whether or not the playlist will loop; true by default
    var looping = true
    //Getters and setters for the music volume
    var volume
        get() = this.music.map { it.volume }.average().toFloat()
        set(value) = this.music.forEach { it.volume = value }

    /**
     * Initializes; starts playing the music
     * Starts with the first track
     */
    init {
        this.start()
    }

    /**
     * Starts playing the music from where it was last left off
     */
    fun start() {
        if (music.isNotEmpty()) {
            this.stopped = false
            music[musicPlaying].play()
        }
    }

    /**
     * Stops the music player
     */
    fun stop() {
        this.music[musicPlaying].stop()
        this.stopped = true
    }

    /**
     * Adds a music to the MusicPlayer
     */
    fun addMusic(path: String) {
        val newMusic = Gdx.audio.newMusic(Gdx.files.internal(path))
        newMusic.volume = this.volume
        this.music.add(newMusic)
    }

    /**
     * Removes a track from the list of music
     * If the track removed is the one currently playing, update the player
     */
    fun removeMusic(index: Int) {
        this.music.remove(this.music[index])
        if (musicPlaying >= index) {
            this.musicPlaying -= 1
        }
        if (musicPlaying == index) {
            this.update()
        }
    }

    /**
     * Updates the music
     * If the currently playing track is no longer playing, move on to the next track
     * and play it
     * If the end of the list is reached and the music does not loop, stop the player
     */
    fun update() {
        if (this.stopped || this.music.isEmpty() || this.music[this.musicPlaying].isPlaying) {
            return
        }
        this.musicPlaying += 1
        if (!this.looping && this.musicPlaying >= this.music.size) {
            this.musicPlaying = 0
            this.stop()
        } else {
            this.musicPlaying %= this.music.size
            this.music[musicPlaying].play()
        }
    }

    /**
     * Disposes of the music player by disposing of all of the contained music
     */
    fun dispose() {
        this.music[musicPlaying].stop()
        this.music.forEach { it.dispose() }
    }

}