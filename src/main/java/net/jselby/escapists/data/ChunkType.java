package net.jselby.escapists.data;

/**
 * The types of chunks that can be seen.
 *
 * @author matpow2
 * @url https://github.com/matpow2/anaconda
 */
public enum ChunkType {
    // Vitalize chunks (0x11)
    VitalizePreview(4386), // Preview
    // Application chunks (0x22)
    // not-in-use 8738AppMiniHeader(), // AppMiniHeader
    AppHeader(8739),
    AppName(8740),
    AppAuthor(8741),
    AppMenu(8742),
    ExtPath(8743),
    // deprecated 8744createPreservingLoader(), // Extensions
    FrameItems(8745), // FrameItems
    // deprecated 8746createPreservingLoader(), // GlobalEvents
    FrameHandles(8747), // FrameHandles
    ExtData(8748), // ExtData
    // deprecated 8749createPreservingLoader(), // Additional_Extension
    EditorFilename(8750), // AppEditorFilename
    TargetFilename(8751), // AppTargetFilename
    AppDoc(8752), // AppDoc
    OtherExtensions(8753), // OtherExts
    GlobalValues(8754),
    GlobalStrings(8755), // GlobalStrings
    ExtensionList(8756), // Extensions2
    AppIcon(8757), // AppIcon_16x16x8
    DemoVersion(8758), // DemoVersion
    SecNum(8759), // serial number
    BinaryFiles(8760), // BinaryFiles
    AppMenuImages(8761), // AppMenuImages
    AboutText(8762), // AboutText
    Copyright(8763), // Copyright
    GlobalValueNames(8764), // GlobalValueNames
    GlobalStringNames(8765), // GlobalStringNames
    MovementExtensions(8766), // MvtExts
    FrameItems_2(8767), // FrameItems_2
    ExeOnly(8768), // EXEOnly
    Protection(8770),
    Shaders(8771), // Shaders
    ExtendedHeader(8773), // ExtendedHeader aka APPHEADER2
    // Frame chunks (0x33)
    Frame(13107), // Frame
    FrameHeader(13108), // FrameHeader
    FrameName(13109), // FrameName
    FramePassword(13110), // FramePassword
    FramePalette(13111), // FramePalette
    ObjectInstances(13112), // FrameItemInstances
    FrameFadeInFrame(13113), // FrameFadeInFrame
    FrameFadeOutFrame(13114), // FrameFadeOutFrame
    FadeIn(13115), // FrameFadeIn
    FadeOut(13116), // FrameFadeOut
    Events(13117), // FrameEvents
    FramePlayHeader(13118), // FramePlayHeader
    Additional_FrameItem(13119), // Additional_FrameItem
    Additional_FrameItemInstance(13120), // Additional_FrameItemInstance
    Layers(13121), // FrameLayers
    VirtualSize(13122), // FrameVirtualRect
    DemoFilePath(13123), // DemoFilePath
    RandomSeed(13124), // RandomSeed
    LayerEffects(13125), // FrameLayerEffects
    BluRayFrameOptions(13126), // BluRayFrameOptions
    MovementTimerBase(13127), // MvtTimerBase
    MosaicImageTable(13128), // MosaicImageTable
    FrameEffects(13129), // FrameEffects
    FrameIphoneOptions(13130), // FrameIphoneOptions
    // Object chunks (0x44)
    ObjectHeader(17476), // ObjInfoHeader
    ObjectName(17477),
    ObjectProperties(17478), // ObjectsCommon
    ObjectUnknown(17479), // ObjectUnknown
    ObjectEffects(17480), // ObjectUnknown2
    // Offset chunks (0x55)
    ImageOffsets(21845), // ImagesOffsets
    FontOffsets(21846), // FontsOffsets
    SoundOffsets(21847), // SoundsOffsets
    MusicOffsets(21848), // MusicsOffsets
    // Bank chunks (0x66)
    ImageBank(26214), // Images
    FontBank(26215),
    SoundBank(26216),
    MusicBank(26217), // Musics
    // Last chunk (0x7f7f)
    Last(32639),
    Unknown(-1); // Last

    private int id;

    ChunkType(int id) {
        this.id = id;
    }

    /**
     * Returns this chunk's ID.
     *
     * @return A chunk ID
     */
    public int getID() {
        return id;
    }

    public static ChunkType getTypeForID(int id) {
        for (ChunkType types : ChunkType.values()) {
            if (types.getID() == id) {
                return types;
            }
        }
        return Unknown;
    }
}
