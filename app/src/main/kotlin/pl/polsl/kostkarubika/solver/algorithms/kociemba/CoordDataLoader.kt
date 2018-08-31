package pl.polsl.kostkarubika.solver.algorithms.kociemba

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class CoordDataLoader(private val file: File) {

    @Throws(IOException::class)
    fun write(coordTransformer: CoordTransformer) {
        val output = DataOutputStream(BufferedOutputStream(FileOutputStream(file)))

        writeShortTwoArray(output, coordTransformer.twistMove)
        writeShortTwoArray(output, coordTransformer.flipMove)
        writeShortTwoArray(output, coordTransformer.FRtoBR_Move)
        writeShortTwoArray(output, coordTransformer.URFtoDLF_Move)
        writeShortTwoArray(output, coordTransformer.URtoDF_Move)
        writeShortTwoArray(output, coordTransformer.URtoUL_Move)
        writeShortTwoArray(output, coordTransformer.UBtoDF_Move)
        writeShortTwoArray(output, coordTransformer.MergeURtoULandUBtoDF)
        output.write(coordTransformer.Slice_URFtoDLF_Parity_Prun)
        output.write(coordTransformer.Slice_URtoDF_Parity_Prun)
        output.write(coordTransformer.Slice_Twist_Prun)
        output.write(coordTransformer.Slice_Flip_Prun)
    }

    @Throws(IOException::class)
    private fun writeShortTwoArray(outputStreams: DataOutputStream, array: Array<ShortArray>) {
        for (i in array) {
            for (j in i) {
                outputStreams.writeShort(j.toInt())
            }
        }
    }

    @Throws(IOException::class)
    fun read(coordTransformer: CoordTransformer) {
        val input = DataInputStream(BufferedInputStream(FileInputStream(file)))

        readShortTwoArray(input, coordTransformer.twistMove)
        readShortTwoArray(input, coordTransformer.flipMove)
        readShortTwoArray(input, coordTransformer.FRtoBR_Move)
        readShortTwoArray(input, coordTransformer.URFtoDLF_Move)
        readShortTwoArray(input, coordTransformer.URtoDF_Move)
        readShortTwoArray(input, coordTransformer.URtoUL_Move)
        readShortTwoArray(input, coordTransformer.UBtoDF_Move)
        readShortTwoArray(input, coordTransformer.MergeURtoULandUBtoDF)
        input.read(coordTransformer.Slice_URFtoDLF_Parity_Prun)
        input.read(coordTransformer.Slice_URtoDF_Parity_Prun)
        input.read(coordTransformer.Slice_Twist_Prun)
        input.read(coordTransformer.Slice_Flip_Prun)
    }

    @Throws(IOException::class)
    private fun readShortTwoArray(inputStream: DataInputStream, array: Array<ShortArray>) {
        for (i in array.indices) {
            for (j in 0..array[i].size - 1) {
                array[i][j] = inputStream.readShort()
            }
        }
    }
}
