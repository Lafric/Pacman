package ingameObjects;

/** static object in the level that the player can't move through */
public class Wall extends IngameObject{
    /**
     * initializes the position of the Object.
     *
     * @param position_x x Position of the Object in the level.
     * @param position_y y Position of the Object in the level.
     */
    public Wall(int position_x, int position_y) {
        super(position_x, position_y);
        this.sprite = "/resources/wall_empty.png";
    }

    /**
     * chooses sprites of the wall-blocks based on the neighboured walls.
     * @param isBlockNorth true if another wall is in the north of this wall, false if not.
     * @param isBlockSouth true if another wall is in the south of this wall, false if not.
     * @param isBlockWest true if another wall is in the west of this wall, false if not.
     * @param isBlockEast true if another wall is in the east of this wall, false if not.
     * @param isBlockNorthWest true if another wall is in the northwest of this wall, false if not.
     * @param isBlockNorthEast true if another wall is in the northeast of this wall, false if not.
     * @param isBlockSouthWest true if another wall is in the southwest of this wall, false if not.
     * @param isBlockSouthEast true if another wall is in the southeast of this wall, false if not.
     */
    public void setSprite(boolean isBlockNorth, boolean isBlockSouth, boolean isBlockWest, boolean isBlockEast,
                          boolean isBlockNorthWest, boolean isBlockNorthEast, boolean isBlockSouthWest, boolean isBlockSouthEast){

        //testing to adjust wall sprites

        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest){
            this.sprite = "/resources/wall_empty.png";
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && isBlockSouthWest && !isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall7.png";
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && !isBlockSouthWest && isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall7.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && isBlockSouthWest && isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall7.png";
            this.spriteQuadrantRotation = 1;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && !isBlockSouthWest && !isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall7.png";
            this.spriteQuadrantRotation = -1;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && !isBlockSouthWest && !isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall6.png";
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && isBlockSouthWest && isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall8.png";
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && isBlockSouthWest && !isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall8.png";
            this.spriteQuadrantRotation = -1;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && !isBlockSouthWest && isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall8.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && isBlockSouthWest && isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall8.png";
            this.spriteQuadrantRotation = 1;
        }


        if(!isBlockNorth && isBlockSouth && isBlockEast && isBlockWest  && isBlockSouthEast && isBlockSouthWest){
            this.sprite = "/resources/wall3.png";
        }
        if(!isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && isBlockSouthWest){
            this.sprite = "/resources/wall4.png";
            spriteQuadrantRotation = -1;
            isSpriteMirrored = true;
        }
        if(!isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && isBlockSouthEast && !isBlockSouthWest){
            this.sprite = "/resources/wall4.png";
            spriteQuadrantRotation = 1;
        }
        if(!isBlockNorth && isBlockSouth && isBlockEast && isBlockWest && !isBlockSouthEast && !isBlockSouthWest){
            this.sprite = "/resources/wall5.png";
            spriteQuadrantRotation = 1;
        }

        if(isBlockNorth && !isBlockSouth && isBlockEast && isBlockWest && isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall3.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && !isBlockSouth && isBlockEast && isBlockWest && !isBlockNorthWest && isBlockNorthEast){
            this.sprite = "/resources/wall4.png";
            this.spriteQuadrantRotation = 1;
            this.isSpriteMirrored = true;
        }
        if(isBlockNorth && !isBlockSouth && isBlockEast && isBlockWest && isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall4.png";
            this.spriteQuadrantRotation = -1;
            //this.isSpriteMirrored = true;
        }
        if(isBlockNorth && !isBlockSouth && isBlockEast && isBlockWest && !isBlockNorthWest && !isBlockNorthEast){
            this.sprite = "/resources/wall5.png";
            this.spriteQuadrantRotation = -1;
        }

        if(isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && isBlockNorthEast && isBlockSouthEast){
            this.sprite = "/resources/wall3.png";
            this.spriteQuadrantRotation = -1;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && !isBlockNorthEast && isBlockSouthEast){
            this.sprite = "/resources/wall4.png";
            this.spriteQuadrantRotation = 2;
            this.isSpriteMirrored = true;
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && isBlockNorthEast && !isBlockSouthEast){
            this.sprite = "/resources/wall4.png";
        }
        if(isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && !isBlockNorthEast && !isBlockSouthEast){
            this.sprite = "/resources/wall5.png";

        }


        if(isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && isBlockNorthWest && isBlockSouthWest){
            this.sprite = "/resources/wall3.png";
            this.spriteQuadrantRotation = 1;
        }
        if(isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && !isBlockNorthWest && isBlockSouthWest){
            this.sprite = "/resources/wall4.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && isBlockNorthWest && !isBlockSouthWest){
            this.sprite = "/resources/wall4.png";
            this.isSpriteMirrored = true;
        }
        if(isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && !isBlockNorthWest && !isBlockSouthWest){
            this.sprite = "/resources/wall5.png";
            this.spriteQuadrantRotation = 2;
        }

        if(!isBlockNorth && !isBlockSouth && isBlockEast && isBlockWest){
            this.sprite = "/resources/wall2.png";
        }
        if(isBlockNorth && isBlockSouth && !isBlockEast && !isBlockWest){
            this.sprite = "/resources/wall2.png";
            this.spriteQuadrantRotation = 1;
        }


        if(!isBlockNorth && !isBlockSouth && !isBlockEast && isBlockWest){
            this.sprite = "/resources/wall1.png";
        }
        if(!isBlockNorth && isBlockSouth && !isBlockEast && !isBlockWest){
            this.sprite = "/resources/wall1.png";
            //this.spriteTransformation = AffineTransform.getQuadrantRotateInstance(1);
            this.spriteQuadrantRotation = -1;
        }
        if(!isBlockNorth && !isBlockSouth && isBlockEast && !isBlockWest){
            this.sprite = "/resources/wall1.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && !isBlockSouth && !isBlockEast && !isBlockWest){
            this.sprite = "/resources/wall1.png";
            this.spriteQuadrantRotation = 1;
        }

        //corner
        if(!isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && !isBlockSouthWest){
            this.sprite = "/resources/wall_corner2.png";
            this.spriteQuadrantRotation = 1;
        }
        if(!isBlockNorth && isBlockSouth && !isBlockEast && isBlockWest && isBlockSouthWest){
            //this.sprite = "/resources/wall_corner3.png";
            this.sprite = "/resources/wall_corner.png";
            this.spriteQuadrantRotation = 1;
        }
        if(!isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && !isBlockSouthEast){
            this.sprite = "/resources/wall_corner2.png";
        }
        if(!isBlockNorth && isBlockSouth && isBlockEast && !isBlockWest && isBlockSouthEast){
            //this.sprite = "/resources/wall_corner3.png";
            this.sprite = "/resources/wall_corner.png";
        }
        if(isBlockNorth && !isBlockSouth && !isBlockEast && isBlockWest && !isBlockNorthWest){
            this.sprite = "/resources/wall_corner2.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && !isBlockSouth && !isBlockEast && isBlockWest && isBlockNorthWest){
            //this.sprite = "/resources/wall_corner3.png";
            this.sprite = "/resources/wall_corner.png";
            this.spriteQuadrantRotation = 2;
        }
        if(isBlockNorth && !isBlockSouth && isBlockEast && !isBlockWest && !isBlockNorthEast){
            this.sprite = "/resources/wall_corner2.png";
            this.spriteQuadrantRotation = 2;
            this.isSpriteMirrored = true;
        }
        if(isBlockNorth && !isBlockSouth && isBlockEast && !isBlockWest && isBlockNorthEast){
            //this.sprite = "/resources/wall_corner3.png";
            this.sprite = "/resources/wall_corner.png";
            this.spriteQuadrantRotation = 2;
            this.isSpriteMirrored = true;
        }

        if(!isBlockNorth && !isBlockSouth && !isBlockEast && !isBlockWest) {
            this.sprite = "/resources/wall9.png";
        }
    }
}
