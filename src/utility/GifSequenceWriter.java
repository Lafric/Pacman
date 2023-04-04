package utility;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

/**
 * utility class for creating temporary .gif files.
 * <p>
 * based on the example found here: <a href="https://memorynotfound.com/generate-gif-image-java-delay-infinite-loop-example">...</a>
 */
public final class GifSequenceWriter {
    /**
     * creates an infinitely looped animation with a specified delay inbetween in form of a gif file out of 2 Images.
     * @param firstPath relative path to the first image of the animation.
     * @param secondPath relative path to the second image of the animation.
     * @param delay time (in ms) between the change of the 2 images.
     * @return path(absolute) to the temporary .gif file, which was created.
     */
    public static String createGIF(String firstPath, String secondPath, int delay){
    //public static Image createGIF(String firstPath, String secondPath, int delay){

        try {
            //read first image
            BufferedImage first = ImageIO.read(Objects.requireNonNull(GifSequenceWriter.class.getResource(firstPath)));
            //read second image
            BufferedImage second = ImageIO.read(Objects.requireNonNull(GifSequenceWriter.class.getResource(secondPath)));


            ImageWriter writer = ImageIO.getImageWritersBySuffix("gif").next();

            //get defaultMetadata of the writer
            ImageWriteParam params = writer.getDefaultWriteParam();
            ImageTypeSpecifier imageTypeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(first.getType());
            IIOMetadata metadata = writer.getDefaultImageMetadata(imageTypeSpecifier, params);


            //get a DOM tree of the metadata to do manipulations
            String metaFormatName = metadata.getNativeMetadataFormatName();
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

            // information on the metadata https://docs.oracle.com/javase/8/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
            // "The graphic control extension contains information on how the frame is to be incorporated into the animation."
            IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

            //"The disposal method indicates whether the current frame should remain in place (doNotDispose),
            // be restored to the backgound color as specified in the stream metadata (restoreToBackgroundColor),
            // or be restored to the previous frame contents (restoreToPrevious) prior to displaying the subsequent frame."
            graphicsControlExtensionNode.setAttribute("disposalMethod", "restoreToBackgroundColor");

            //"True if the frame should be advanced based on user input"
            //graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");

            //delayTime is  specified in 10^(-2) seconds -> /10 for delay in ms
            graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(delay/10));

            //"True if a transparent color exists"
            graphicsControlExtensionNode.setAttribute("transparentColorFlag", "TRUE");

            //"The transparent color, if transparentColorFlag is true"
            //graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

            //IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
            //commentsNode.setAttribute("CommentExtension", "Created by: https://memorynotfound.com");


            IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
            IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

            child.setAttribute("applicationID", "NETSCAPE");
            child.setAttribute("authenticationCode", "2.0");


            //int loopContinuously = loop ? 0 : 1;
            //child.setUserObject(new byte[]{ 0x1, (byte) (loopContinuously & 0xFF), (byte) ((loopContinuously >> 8) & 0xFF)});
            child.setUserObject(new byte[]{ 0x1, 0, 0});
            appExtensionsNode.appendChild(child);
            //apply changes to internal metadata object
            metadata.setFromTree(metaFormatName, root);

            //create a temporary .gif file, that we can write to.
            File tmpFile = File.createTempFile("hungryBallAnimation", ".gif");


            //write the images onto the outputsream
            ImageOutputStream output = new FileImageOutputStream(tmpFile);

            //write to the temporary gif file
            writer.setOutput(output);

            writer.prepareWriteSequence(null);

            writer.writeToSequence(new IIOImage(first, null, metadata), params);
            writer.writeToSequence(new IIOImage(second, null, metadata), params);




            //test(use stream instead of a tmp file and return image instead. Does not work yet (only the first frame of the image is preserved)).
/*
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ImageOutputStream output = new MemoryCacheImageOutputStream(byteOutput);


            //write to the stream
            writer.setOutput(output);

            writer.prepareWriteSequence(null);

            writer.writeToSequence(new IIOImage(first, null, metadata), params);
            writer.writeToSequence(new IIOImage(second, null, metadata), params);

            output.flush();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
            //ImageInputStream iis = new MemoryCacheImageInputStream(inputStream);
            //new SequenceInputStream();
            //new InputStreamReader().
            //new DataInputStream()
            //new FileCacheImageInputStream()

            //ImageIO.read and the ImageReader do not preserve the animation of the gif (only the first frame is displayed)
            BufferedImage res = ImageIO.read(inputStream);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            ImageReader reader = readers.next();
            //ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();

            reader.setInput(iis, true);
            //Image res = reader.read(0);
            //Image res = (Image)reader.getInput();

            //Image res = ImageIO.read(iis);
            inputStream.close();


            //System.out.println(first.getWidth());
            //System.out.println(res.getWidth());
            */




            writer.endWriteSequence();
            writer.dispose();
            //output.flush();
            output.close();


            //return "";
            //return res;
            return tmpFile.getAbsolutePath();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * returns a node under the root node with a specified name.
     * If the node with the given name does not exist yet, a new node with this name is created and added as child of the root node.
     * @param rootNode first node in the DOM tree.
     * @param nodeName name of the node which is to be returned.
     * @return a node matching the given name.
     */
    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++){
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }

}
