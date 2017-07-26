package reed.flyingreed.widget

import java.nio.FloatBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Created by thinkreed on 2017/7/26.
 */
class DemoTriangle {
    private val vertexBuffer: FloatBuffer

    var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    init {
        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.size * 4)
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder())

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer()
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords)
        // set the buffer to read the first coordinate
        vertexBuffer.position(0)
    }


    companion object {
        internal val COORDS_PER_VERTEX = 3
        internal var triangleCoords = floatArrayOf(// in counterclockwise order:
                0.0f, 0.622008459f, 0.0f, // top
                -0.5f, -0.311004243f, 0.0f, // bottom left
                0.5f, -0.311004243f, 0.0f  // bottom right
        )
    }
}