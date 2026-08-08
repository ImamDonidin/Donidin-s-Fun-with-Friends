package com.donidin.funwithfriends.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public abstract class CustomHeadLayerMixin<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> {

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderCustomFlowerHat(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                       T entity, float limbSwing, float limbSwingAmount,
                                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch,
                                       CallbackInfo ci) {

        ItemStack headStack = entity.getItemBySlot(EquipmentSlot.HEAD);

        if (!headStack.isEmpty() && headStack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();

            boolean isBush = block instanceof BushBlock;
            boolean isPot = block instanceof FlowerPotBlock;
            boolean isPickle = block instanceof SeaPickleBlock;
            boolean isCandle = block instanceof CandleBlock;
            boolean isDripstone = block instanceof PointedDripstoneBlock;
            boolean isEndRod = block instanceof EndRodBlock;
            boolean isPlate = block instanceof BasePressurePlateBlock;

            if (isBush || isPot || isPickle || isCandle || isDripstone || isEndRod || isPlate) {
                BlockState state = block.defaultBlockState();

                if (block instanceof TallFlowerBlock && state.hasProperty(TallFlowerBlock.HALF)) {
                    state = state.setValue(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER);
                } else if (isPickle && state.hasProperty(SeaPickleBlock.PICKLES)) {
                    state = state.setValue(SeaPickleBlock.PICKLES, 1);
                } else if (isCandle && state.hasProperty(CandleBlock.CANDLES)) {
                    BlockItemStateProperties blockStateData = headStack.get(DataComponents.BLOCK_STATE);
                    if (blockStateData != null && blockStateData.get(CandleBlock.CANDLES) != null) {
                        state = state.setValue(CandleBlock.CANDLES, blockStateData.get(CandleBlock.CANDLES));
                    } else {
                        int candlesCount = Mth.clamp(headStack.getCount(), 1, 4);
                        state = state.setValue(CandleBlock.CANDLES, candlesCount);
                    }
                } else if (isDripstone && state.hasProperty(PointedDripstoneBlock.THICKNESS)) {
                    state = state.setValue(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP);
                }

                if (state.getRenderShape() == RenderShape.MODEL) {
                    poseStack.pushPose();

                    @SuppressWarnings("unchecked")
                    CustomHeadLayer<T, M> layer = (CustomHeadLayer<T, M>) (Object) this;
                    layer.getParentModel().getHead().translateAndRotate(poseStack);

                    float scale = 0.625F;
                    double yOffset = -0.25D;

                    if (block instanceof TallFlowerBlock) {
                        scale = 0.5F;
                        yOffset = -0.35D;
                    } else if (isPot) {
                        scale = 0.625F;
                        yOffset = -0.25D;
                    } else if (isPickle) {
                        scale = 0.75F;
                        yOffset = -0.15D;
                    } else if (isCandle) {
                        scale = 0.65F;
                        yOffset = -0.20D;
                    } else if (isDripstone) {
                        scale = 0.625F;
                        yOffset = -0.25D;
                    } else if (isEndRod) {
                        scale = 0.6F;
                        yOffset = -0.15D;
                    } else if (isPlate) {
                        scale = 0.75F;
                        yOffset = -0.05D;
                    }

                    poseStack.translate(0.0D, yOffset, 0.0D);
                    poseStack.scale(scale, scale, scale);
                    poseStack.translate(-0.5D, -0.5D, -0.5D);

                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                            state,
                            poseStack,
                            buffer,
                            packedLight,
                            OverlayTexture.NO_OVERLAY
                    );

                    poseStack.popPose();

                    ci.cancel();
                }
            }
        }
    }
}