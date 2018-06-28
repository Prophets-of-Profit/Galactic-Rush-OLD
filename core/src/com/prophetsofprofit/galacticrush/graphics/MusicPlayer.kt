package com.prophetsofprofit.galacticrush.graphics

import com.badlogic.gdx.Gdx

/**
 * A class which handles the playing of multiple tracks of music
 * Takes in a list of paths to the music
 */
class MusicPlayer(musicPath: Array<String>) {

    //The list of music to be played by this music player
    val music = Array(musicPath.size) { Gdx.audio.newMusic(Gdx.files.internal(musicPath[it])) }
    //The index of music playing
    var musicPlaying = 0

    /**
     * Initializes; starts playing the first music in the list
     */
    init {
        if (music.isNotEmpty()) music[0].play()
    }

    /**
     * Updates the music
     * If the currently playing track is no longer playing, move on to the next track
     * and play it
     */
    fun update() {
        if (!this.music[this.musicPlaying].isPlaying) {
            this.musicPlaying += 1
            this.musicPlaying %= music.size
            this.music[musicPlaying].play()
        }
    }

}