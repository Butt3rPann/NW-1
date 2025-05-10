<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { deleteItemById, getItemById } from '@/libs/fetchUtils.js'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import ItemDetailRow from '@/components/sale-item/sale-item-detail/ItemDetailRow.vue'
import LinkButton from '@/components/elements/LinkButton.vue'
import backArrow from '@/assets/images/backArrow.png'
import SaleItemNotFound from '@/components/sale-item/SaleItemNotFound.vue'
import { formatLocalTime } from '@/libs/datetimeUtils'
import editIcon from "@/assets/images/edit.png";
import router from '@/router'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import trashIcon from "@/assets/images/trash.png"
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'

const { params: { saleItemId } } = useRoute()

const selectedItem = ref({})

async function getSaleItemById() {
    try {
        selectedItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, saleItemId)
        selectedItem.value.createdOn = formatLocalTime(selectedItem.value.createdOn);
        selectedItem.value.updatedOn = formatLocalTime(selectedItem.value.updatedOn)
    } catch (error) {
        console.log(error)
    }
}

onMounted(() => {
    getSaleItemById()
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

const route = useRoute()

const isShowPopup = ref(false)

if (route.query.edited === 'true') {
    router.replace({ query: { } })
    isShowPopup.value = true
}

async function deleteSaleItem(){
    try {
        await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, saleItemId)
        router.push({name: 'SaleItems', query: {deleted: 'true'}})
    } catch (error) {
        console.log(error);  
    }
}

const showDelConfirm = ref(false)

function closeDelConfirm() {
    showDelConfirm.value = false
}
</script>

<template>
<div>
    <PopupMessage message="The sale item has been updated." :isShowPopup="isShowPopup" class="fixed mt-25"/>
    <div v-if="selectedItem?.id">
        <div class="p-21 pt-9 mt-21 font-rubik relative">
            <LinkButton :icon="backArrow" alt="backArrow" text="Back to product list" :to="'/sale-items'"
                textColor="text-[#6F879C]" bgColor="bg-transparent"
                class="mb-7" />
            <div class="itbms-row flex justify-between ">
                <div class="flex flex-col items-center mr-6">
                    <div class="bg-[#F0EDEC] w-119 h-129 rounded-2xl flex items-center justify-center overflow-hidden">
                        <img :src="phones.mainImage" alt="Selected Phone" class="h-87">
                    </div>
                    <div>
                        <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                            @update:selected-index="changeMainImg" />
                    </div>
                </div>
                <div class="flex flex-col ml-7 w-1750">
                    <div class="border-b-3 border-[#E5E8F4] pb-4.5 space-y-3">
                        <p class="itbms-brand text-[#A4A4A3] text-xl">{{ selectedItem.brandName }}</p>
                        <p class="itbms-model text-[#332A1E] font-bold text-4xl">{{ selectedItem.model }}</p>
                        <p class="itbms-description text-[#6F879C] text-xl font-light">{{ selectedItem.description }}</p>
                    </div>
                    <div class="flex items-center justify-between border-b-3 border-[#E5E8F4]">
                        <p class="text-[#6F879C] py-5 font-bold text-4xl">
                            <span class="itbms-price-unit pr-2">Bath</span>
                            <span class="itbms-price">{{ selectedItem.price?.toLocaleString() }}</span>
                        </p>
                        <div class="flex items-center">
                            <img src="@/assets/images/box.png" alt="box" class="w-8 mr-3">
                            <p :class="[selectedItem.quantity > 0 ? 'bg-[#97C5B8] text-[#225528]' : 'bg-[#EAA9A9] text-[#680D0D]']"
                                class="p-2 font-bold text-lg rounded-full px-9">
                                <span class="itbms-quantity mr-2">{{ selectedItem.quantity }}</span>
                                <span class="itbms-quantity-unit">items in stock</span>
                            </p>
                        </div>
                    </div>
                    <div class="text-[#332A1E] pb-3">
                        <p class="pt-4.5 font-bold text-[1.45rem]">Product Description</p>
                        <ItemDetailRow label="Brand" :value="selectedItem.brandName" />
                        <ItemDetailRow label="Model" :value="selectedItem.model" />
                        <ItemDetailRow label="StorageGb" :value="selectedItem.storageGb" unit="GB" valueClass="itbms-storageGb" unitClass=" itbms-storageGb-unit" />
                        <ItemDetailRow label="RamGb" :value="selectedItem.ramGb" unit="GB" valueClass=" itbms-ramGb" unitClass="itbms-ramGb-unit" />
                        <ItemDetailRow label="ScreenSizeInch" :value="selectedItem.screenSizeInch" unit="Inches" valueClass="itbms-screenSizeInch" unitClass="itbms-screenSizeInch-unit" />
                        <ItemDetailRow label="Color" :value="selectedItem.color" valueClass="itbms-color" />
                    </div>
                    <div class="flex justify-center items-center gap-10 m-4">
                        <LinkButton :icon="editIcon" text="Edit" bgColor="bg-[#FFFFFF]" textColor="text-[#6F879C]" :to="`/sale-items/${saleItemId}/edit`" class="itbms-edit-button"/>
                        <LinkButton :icon="trashIcon" text="Delete" bgColor="bg-[#FFFFFF]" textColor="text-[#D27B7B]" class="itbms-delete-button border-2 border-[#D27B7B]" @click="showDelConfirm = true"/>
                    </div>
                    <div class="text-sm space-y-1 flex justify-between mt-3">
                        <div class="flex gap-1">
                            <p class="font-semibold text-[#332A1E]/70 mr-3">Created On:</p>
                            <p class="text-[#332A1E]/40">{{ selectedItem.createdOn }}</p>
                        </div>
                        <div class="flex gap-1">
                            <p class="font-semibold text-[#332A1E]/70 mr-3">Updated On:</p>
                            <p class="text-[#332A1E]/40">{{ selectedItem.updatedOn }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
    <SaleItemNotFound v-else/>
</div>
</template>

<style scoped></style>
