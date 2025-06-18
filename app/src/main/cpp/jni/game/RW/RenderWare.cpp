#include "../../main.h"
#include "RenderWare.h"
#include "dependencies/armhook/patch.h"

RsGlobalType* RsGlobal;

/* rwcore.h */
RwCamera* (*RwCameraBeginUpdate)(RwCamera* camera);
RwCamera* (*RwCameraShowRaster)(RwCamera * camera, void *pDev, RwUInt32 flags);

RwRaster* 	(*RwRasterCreate)(RwInt32 width, RwInt32 height, RwInt32 depth, RwInt32 flags);
RwBool 		(*RwRasterDestroy)(RwRaster * raster);
RwRaster* 	(*RwRasterGetOffset)(RwRaster *raster, RwInt16 *xOffset, RwInt16 *yOffset);
RwInt32 	(*RwRasterGetNumLevels)(RwRaster * raster);
RwRaster* 	(*RwRasterSubRaster)(RwRaster * subRaster, RwRaster * raster, RwRect * rect);
RwRaster* 	(*RwRasterRenderFast)(RwRaster * raster, RwInt32 x, RwInt32 y);
RwRaster* 	(*RwRasterRender)(RwRaster * raster, RwInt32 x, RwInt32 y);
RwRaster* 	(*RwRasterRenderScaled)(RwRaster * raster, RwRect * rect);
RwRaster* 	(*RwRasterPushContext)(RwRaster * raster);
RwRaster* 	(*RwRasterPopContext)(void);
RwRaster* 	(*RwRasterGetCurrentContext)(void);
RwBool 		(*RwRasterClear)(RwInt32 pixelValue);
RwBool 		(*RwRasterClearRect)(RwRect * rpRect, RwInt32 pixelValue);
RwRaster* 	(*RwRasterShowRaster)(RwRaster * raster, void *dev, RwUInt32 flags);
RwUInt8* 	(*RwRasterLock)(RwRaster * raster, RwUInt8 level, RwInt32 lockMode);
RwRaster* 	(*RwRasterUnlock)(RwRaster * raster);
RwUInt8* 	(*RwRasterLockPalette)(RwRaster * raster, RwInt32 lockMode);
RwRaster* 	(*RwRasterUnlockPalette)(RwRaster * raster);
RwImage* 	(*RwImageCreate)(RwInt32 width, RwInt32 height, RwInt32 depth);
RwBool 		(*RwImageDestroy)(RwImage * image);
RwImage* 	(*RwImageAllocatePixels)(RwImage * image);
RwImage* 	(*RwImageFreePixels)(RwImage * image);
RwImage* 	(*RwImageCopy)(RwImage * destImage, const RwImage * sourceImage);
RwImage* 	(*RwImageResize)(RwImage * image, RwInt32 width, RwInt32 height);
RwImage* 	(*RwImageApplyMask)(RwImage * image, const RwImage * mask);
RwImage* 	(*RwImageMakeMask)(RwImage * image);
RwImage* 	(*RwImageReadMaskedImage)(const RwChar * imageName, const RwChar * maskname);
RwImage* 	(*RwImageRead)(const RwChar * imageName);
RwImage* 	(*RwImageWrite)(RwImage * image, const RwChar * imageName);
RwImage* 	(*RwImageSetFromRaster)(RwImage *image, RwRaster *raster);
RwRaster* 	(*RwRasterSetFromImage)(RwRaster *raster, RwImage *image);
RwRaster* 	(*RwRasterRead)(const RwChar *filename);
RwRaster* 	(*RwRasterReadMaskedRaster)(const RwChar *filename, const RwChar *maskname);
RwImage* 	(*RwImageFindRasterFormat)(RwImage *ipImage, RwInt32 nRasterType, RwInt32 *npWidth, RwInt32 *npHeight, RwInt32 *npDepth,RwInt32 *npFormat);

/* rwlpcore.h */
RwReal 		(*RwIm2DGetNearScreenZ)(void);
RwReal 		(*RwIm2DGetFarScreenZ)(void);
RwBool 		(*RwRenderStateGet)(RwRenderState state, void *value);
RwBool 		(*RwRenderStateSet)(RwRenderState state, void *value);
RwBool 		(*RwIm2DRenderLine)(RwIm2DVertex *vertices, RwInt32 numVertices, RwInt32 vert1, RwInt32 vert2);
RwBool 		(*RwIm2DRenderTriangle)(RwIm2DVertex *vertices, RwInt32 numVertices, RwInt32 vert1, RwInt32 vert2, RwInt32 vert3 );
RwBool 		(*RwIm2DRenderPrimitive)(RwPrimitiveType primType, RwIm2DVertex *vertices, RwInt32 numVertices);
RwBool 		(*RwIm2DRenderIndexedPrimitive)(RwPrimitiveType primType, RwIm2DVertex *vertices, RwInt32 numVertices, RwImVertexIndex *indices, RwInt32 numIndices);

/* rtpng.h */
RwImage*	(*RtPNGImageWrite)(RwImage* image, const RwChar* imageName);
RwImage* 	(*RtPNGImageRead)(const RwChar* imageName);

RwBool (*RwTextureDestroy)(RwTexture* texture);
void WriteMemory(uintptr_t dest, uintptr_t src, size_t size);
void InitRenderWareFunctions()
{
    LOGI(MAKEOBF("Initializing RenderWare.."));
	/* skeleton.h */
	RsGlobal = (RsGlobalType*)(g_libGTAVC + (VER_x32 ? 0x004AC24 : 0x625A0));

	/* rwCore.h */
	*(void**)(&RwCameraBeginUpdate) 			= (void*)(CHook::getSym(OBF("_Z19RwCameraBeginUpdateP8RwCamera")));
	*(void**)(&RwCameraShowRaster)				= (void*)(CHook::getSym(OBF("_Z18RwCameraShowRasterP8RwCameraPvj")));

	*(void **)(&RwRasterCreate) 				= (void*)(CHook::getSym(OBF("_Z14RwRasterCreateiiii")));
	*(void **)(&RwRasterDestroy) 				= (void*)(CHook::getSym(OBF("_Z15RwRasterDestroyP8RwRaster")));
	*(void **)(&RwRasterGetOffset) 				= (void*)(CHook::getSym(OBF("_Z17RwRasterGetOffsetP8RwRasterPsS1_")));
	*(void **)(&RwRasterGetNumLevels) 			= (void*)(CHook::getSym(OBF("_Z20RwRasterGetNumLevelsP8RwRaster")));
	*(void **)(&RwRasterSubRaster) 				= (void*)(CHook::getSym(OBF("_Z17RwRasterSubRasterP8RwRasterS0_P6RwRect")));
	*(void **)(&RwRasterRenderFast)				= (void*)(CHook::getSym(OBF("_Z18RwRasterRenderFastP8RwRasterii")));
	*(void **)(&RwRasterRender)					= (void*)(CHook::getSym(OBF("_Z14RwRasterRenderP8RwRasterii")));
	*(void **)(&RwRasterRenderScaled)			= (void*)(CHook::getSym(OBF("_Z20RwRasterRenderScaledP8RwRasterP6RwRect")));
	*(void **)(&RwRasterPushContext)			= (void*)(CHook::getSym(OBF("_Z19RwRasterPushContextP8RwRaster")));
	*(void **)(&RwRasterPopContext)				= (void*)(CHook::getSym(OBF("_Z18RwRasterPopContextv")));
	*(void **)(&RwRasterGetCurrentContext)		= (void*)(CHook::getSym(OBF("_Z25RwRasterGetCurrentContextv")));
	*(void **)(&RwRasterClear)					= (void*)(CHook::getSym(OBF("_Z13RwRasterCleari")));
	*(void **)(&RwRasterClearRect)				= (void*)(CHook::getSym(OBF("_Z17RwRasterClearRectP6RwRecti")));
	*(void **)(&RwRasterShowRaster)				= (void*)(CHook::getSym(OBF("_Z18RwRasterShowRasterP8RwRasterPvj")));
	*(void **)(&RwRasterLock)					= (void*)(CHook::getSym(OBF("_Z12RwRasterLockP8RwRasterhi")));
	*(void **)(&RwRasterUnlock)					= (void*)(CHook::getSym(OBF("_Z14RwRasterUnlockP8RwRaster")));
	*(void **)(&RwRasterLockPalette)			= (void*)(CHook::getSym(OBF("_Z19RwRasterLockPaletteP8RwRasteri")));
	*(void **)(&RwRasterUnlockPalette)			= (void*)(CHook::getSym(OBF("_Z21RwRasterUnlockPaletteP8RwRaster")));
	*(void **)(&RwImageCreate)					= (void*)(CHook::getSym(OBF("_Z13RwImageCreateiii")));
	*(void **)(&RwImageDestroy)					= (void*)(CHook::getSym(OBF("_Z14RwImageDestroyP7RwImage")));
	*(void **)(&RwImageAllocatePixels)			= (void*)(CHook::getSym(OBF("_Z21RwImageAllocatePixelsP7RwImage")));
	*(void **)(&RwImageFreePixels)				= (void*)(CHook::getSym(OBF("_Z17RwImageFreePixelsP7RwImage")));
	*(void **)(&RwImageCopy)					= (void*)(CHook::getSym(OBF("_Z11RwImageCopyP7RwImagePKS_")));
	*(void **)(&RwImageResize)					= (void*)(CHook::getSym(OBF("_Z13RwImageResizeP7RwImageii")));
	*(void **)(&RwImageApplyMask)				= (void*)(CHook::getSym(OBF("_Z16RwImageApplyMaskP7RwImagePKS_")));
	*(void **)(&RwImageMakeMask)				= (void*)(CHook::getSym(OBF("_Z15RwImageMakeMaskP7RwImage")));
	*(void **)(&RwImageReadMaskedImage)			= (void*)(CHook::getSym(OBF("_Z22RwImageReadMaskedImagePKcS0_")));
	*(void **)(&RwImageRead)					= (void*)(CHook::getSym(OBF("_Z11RwImageReadPKc")));
	*(void **)(&RwImageWrite)					= (void*)(CHook::getSym(OBF("_Z12RwImageWriteP7RwImagePKc")));
	*(void **)(&RwImageSetFromRaster)			= (void*)(CHook::getSym(OBF("_Z20RwImageSetFromRasterP7RwImageP8RwRaster")));
	*(void **)(&RwRasterSetFromImage)			= (void*)(CHook::getSym(OBF("_Z20RwRasterSetFromImageP8RwRasterP7RwImage")));
	*(void **)(&RwRasterRead)					= (void*)(CHook::getSym(OBF("_Z12RwRasterReadPKc")));
	*(void **)(&RwRasterReadMaskedRaster)		= (void*)(CHook::getSym(OBF("_Z24RwRasterReadMaskedRasterPKcS0_")));
	*(void **)(&RwImageFindRasterFormat)		= (void*)(CHook::getSym(OBF("_Z23RwImageFindRasterFormatP7RwImageiPiS1_S1_S1_")));

	/* rwlpcore.h */
	*(void **)(&RwIm2DGetNearScreenZ)			= (void*)(CHook::getSym(OBF("_Z20RwIm2DGetNearScreenZv")));
	*(void **)(&RwIm2DGetFarScreenZ)			= (void*)(CHook::getSym(OBF("_Z19RwIm2DGetFarScreenZv")));
	*(void **)(&RwRenderStateGet)				= (void*)(CHook::getSym(OBF("_Z16RwRenderStateGet13RwRenderStatePv")));
	*(void **)(&RwRenderStateSet)				= (void*)(CHook::getSym(OBF("_Z16RwRenderStateSet13RwRenderStatePv")));
	*(void **)(&RwIm2DRenderLine)				= (void*)(CHook::getSym(OBF("_Z16RwIm2DRenderLineP14RwOpenGLVertexiii")));
	*(void **)(&RwIm2DRenderTriangle)			= (void*)(CHook::getSym(OBF("_Z20RwIm2DRenderTriangleP14RwOpenGLVertexiiii")));
	*(void **)(&RwIm2DRenderPrimitive)			= (void*)(CHook::getSym(OBF("_Z21RwIm2DRenderPrimitive15RwPrimitiveTypeP14RwOpenGLVertexi")));
	*(void **)(&RwIm2DRenderIndexedPrimitive)	= (void*)(CHook::getSym(OBF("_Z28RwIm2DRenderIndexedPrimitive15RwPrimitiveTypeP14RwOpenGLVertexiPti")));

	/* rtpng.h */
	//*(void **)(&RtPNGImageWrite)				= (void*)(CHook::getSym(reinterpret_cast<const char *>(g_libGTAVC + (VER_x32 ? 0x2C5494 : 0x3E017C)))));
	//*(void **)(&RtPNGImageRead)					= (void*)(CHook::getSym(reinterpret_cast<const char *>(g_libGTAVC + (VER_x32 ? 0x2C5720 : 0x3E0564)))));

	*(void**)(&RwTextureDestroy) 				= (void*)(CHook::getSym(OBF("_Z16RwTextureDestroyP9RwTexture")));
}