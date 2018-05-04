package com.prophetsofprofit.galacticrush

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Main : ApplicationAdapter() {

    lateinit var batch: SpriteBatch
    lateinit var img: Texture

    override fun create() {
        this.batch = SpriteBatch()
        this.img = Texture("badlogic.jpg")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        this.batch.begin()
        this.batch.draw(img, 0f, 0f)
        this.batch.end()
    }

    override fun dispose() {
        this.batch.dispose()
        this.img.dispose()
    }

}
