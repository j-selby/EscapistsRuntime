package net.jselby.escapists.game

import net.jselby.escapists.data.ObjectDefinition
import net.jselby.escapists.data.chunks.ObjectInstances
import net.jselby.escapists.data.objects.ObjectCommon
import org.mini2Dx.core.graphics.Graphics
import java.util.*

/**
 * A instance of an object within the world.

 * @author j_selby
 */
abstract class ObjectInstance(definition: ObjectDefinition, instance: ObjectInstances.ObjectInstance) {
    /**
     * Returns the name of this object.
     * @return A String value
     */
    val name: String
    /**
     * Returns the ID of this object.
     * @return An object ID
     */
    private val id: Int
    /**
     * Returns the layer that this object exists on. Should not change post init.
     * @return The layer ID of this object.
     */
    val layerID: Int
    /**
     * Returns this Object's ObjectInfo information.
     * @return A ObjectInfo handle.
     */
    val objectInfo: Int

    open var x: Float = 0f
    open var y: Float = 0f

    var animation = 0
    var animationFrame = 0
    var imageAlpha = 255

    var bold = false

    var isVisible = true
    val variables: Map<String, Any> = HashMap()
    val listElements: List<String> = ArrayList()
    var selectedLine = 0
    var loadedFile = ""

    init {
        this.name = definition.name.trim()
        this.id = instance.handle
        this.objectInfo = instance.objectInfo
        this.layerID = instance.layer.toInt()
        this.x = instance.x.toFloat()
        this.y = instance.y.toFloat()

        if (definition.properties.isCommon) {
            isVisible = (definition.properties.properties as ObjectCommon?)!!.isVisibleAtStart
        }
    }

    /**
     * Returns the width of this object
     * @return A width argument
     */
    abstract val width: Float

    /**
     * Returns the height of this object
     * @return A height argument
     */
    abstract val height: Float

    /**
     * Gets the X position of the object as it would be, rendered to the screen.
     * This is independent of cameras, and is simply accounting for centering, etc.
     */
    open fun getScreenX(): Float = x

    /**
     * Gets the Y position of the object as it would be, rendered to the screen.
     * This is independent of cameras, and is simply accounting for centering, etc.
     */
    open fun getScreenY(): Float = y

    /**
     * Ticks this object, accepting input and responding accordingly.
     * @param container The container to poll information from.
     */
    abstract fun tick(container: EscapistsGame)

    /**
     * Draws this object onto the screen.
     * @param container The container to poll information from.
     * *
     * @param g The graphics instance to draw stuff onto.
     */
    abstract fun draw(container: EscapistsGame, g: Graphics)
}
