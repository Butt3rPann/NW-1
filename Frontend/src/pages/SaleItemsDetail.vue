<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getSaleItemById } from '@/libs/fetchUtils.js'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import ItemDetailRow from '@/components/sale-item/sale-item-detail/ItemDetailRow.vue'
import BaseButton from '@/components/BaseButton.vue'
import backArrow from '@/assets/images/backArrow.png'
import homeLogo from '@/assets/images/home.png'

const { params: { saleItemId } } = useRoute()

const selectedItem = ref({})

async function getItemById() {
    try {
        selectedItem.value = await getSaleItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, saleItemId)
    } catch (error) {
        console.log(error)
    }
}

onMounted(() => {
    getItemById()
})

const phones = ref({
    mainImage: '/saleItemImage/demoImg1.png',
    thumbnail: [
        '/saleItemImage/demoImg1.png',
        '/saleItemImage/demoImg2.png',
        '/saleItemImage/demoImg3.png',
        '/saleItemImage/demoImg4.png'
    ]
})

const selectedPhone = ref(0)

const changeMainImg = (index) => {
    selectedPhone.value = index
    phones.value.mainImage = phones.value.thumbnail[selectedPhone.value]
}

</script>

<template>
    <div v-if="selectedItem?.id" class="p-[6vw] pt-[2vw] mt-[6vw] font-rubik">
        <BaseButton :icon="backArrow" alt="backArrow" text="Back to product list" :to="'/sale-items'"
            textColor="text-[#6F879C]" bgColor="bg-transparent"
            class="mb-[2vw]" />
        <div class="itbms-row flex justify-between items-center">
            <div class="flex flex-col items-center mr-[1.7vw]">
                <div class="bg-[#F0EDEC] w-[33vw] h-[36vw] rounded-2xl flex items-center justify-center">
                    <img :src="phones.mainImage" alt="Selected Phone" class="h-[24vw]">
                </div>
                <div>
                    <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                        @update:selected-index="changeMainImg" />
                </div>
            </div>
            <div class="flex flex-col ml-[2vw] w-[500vw]">
                <div class="border-b-[0.2vw] border-[#E5E8F4] pb-[1.2vw]">
                    <p class="itbms-brand text-[#A4A4A3] text-[1.6vw]">{{ selectedItem.brandName }}</p>
                    <p class="itbms-model text-[#332A1E] font-bold text-[2.5vw]">{{ selectedItem.model }}</p>
                    <p class="itbms-description text-[#6F879C] text-[1.4vw] font-light">{{ selectedItem.description }}</p>
                </div>
                <div class="flex items-center justify-between border-b-[0.2vw] border-[#E5E8F4]">
                    <p class="text-[#6F879C] pt-[1.2vw] pb-[1.2vw] font-bold text-[2.6vw]">
                        <span class="itbms-price-unit pr-[0.5vw]">Bath</span>
                        <span class="itbms-price">{{ selectedItem.price?.toLocaleString() }}</span>
                    </p>
                    <div class="flex items-center">
                        <img src="@/assets/images/box.png" alt="box" class="w-[2.2vw] mr-[0.8vw]">
                        <p :class="[selectedItem.quantity > 0 ? 'bg-[#97C5B8] text-[#225528]' : 'bg-[#EAA9A9] text-[#680D0D]']"
                            class="p-[0.5vw] font-bold text-[1.3vw] rounded-full px-[2.5vw]">
                            <span class="itbms-quantity mr-[0.5vw]">{{ selectedItem.quantity }}</span>
                            <span class="itbms-quantity-unit">items in stock</span>
                        </p>
                    </div>
                </div>
                <div class="text-[#332A1E] pb-[0.8vw] border-b-[0.2vw] border-[#E5E8F4]">
                    <p class="pt-[1.2vw] pb-[1.2vw] font-bold text-[1.6vw]">Product Description</p>
                    <div class="flex items-center justify-between text-[1.4vw]">
                        <p class="font-medium">Brand</p>
                        <p>{{ selectedItem.brandName }}</p>
                    </div>
                </div>
                <ItemDetailRow label="Model" :value="selectedItem.model" />
                <ItemDetailRow label="StorageGb" :value="selectedItem.storageGb" unit="GB" valueClass="itbms-storageGb" unitClass=" itbms-storageGb-unit" />
                <ItemDetailRow label="RamGb" :value="selectedItem.ramGb" unit="GB" valueClass=" itbms-ramGb" unitClass="itbms-ramGb-unit" />
                <ItemDetailRow label="ScreenSizeInch" :value="selectedItem.screenSizeInch" unit="Inches" valueClass="itbms-screenSizeInch" unitClass="itbms-screenSizeInch-unit" />
                <ItemDetailRow label="Color" :value="selectedItem.color" valueClass="itbms-color" />
            </div>
        </div>

    </div>
    <div v-else class="flex flex-col md:flex-row items-center justify-center h-screen bg-white px-[2vw] font-rubik">
        <img src="../assets/images/product-not-found.png" alt="Product Not Found"
            class="w-[48vw] h-auto mb-[4vw] md:mb-0 md:mr-[5vw]" />

        <div class="text-center md:text-left">
            <p class="text-[3.9vw] font-bold text-[#332A1E] leading-tight">
                <span>Product</span><br />
                <span>Not Found</span>
            </p>
            <p class="itbms-message text-[1.65vw] text-[#332A1E] mt-[1.0vw]">The requested sale item does not exist.
            </p>

            <div class="flex flex-col md:flex-row gap-[1.5vw] mt-[1.75vw]">
                <BaseButton :icon="homeLogo" alt="homeLogo" text="Back to homepage" :to="'/'" 
                    textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" />
                <BaseButton :icon="backArrow" alt="backArrow" text="Back to product list" :to="'/sale-items'"
                    textColor="text-[#6F879C]" bgColor="bg-transparent"
                    class="itbms-button" />
            </div>
        </div>
    </div>
</template>

<style scoped></style>
