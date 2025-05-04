<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSaleItemById } from '@/libs/fetchUtils.js'
import MainPhone from '@/components/model/MainPhone.vue'
import OptionsPhone from '@/components/model/OptionsPhone.vue'
import ItemDetailRow from '@/components/ItemDetailRow.vue'

const {
    params: { saleItemId }
} = useRoute()

const selectedItem = ref({})

async function getItemById() {
    selectedItem.value = await getSaleItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, saleItemId)
}
onMounted(() => {
  getItemById()
})

const router = useRouter()
const goBack = () => {
    router.go(-1)
}

const selectedPhone = ref(0)
const phones = ref([
  { mainImage: '../../public/saleItemImage/bigDemoImg.png', thumbnail: '../../public/saleItemImage/bigDemoImg.png' },
  { mainImage: '../../public/saleItemImage/bigDemoImg.png', thumbnail: '../../public/saleItemImage/bigDemoImg.png' },
  { mainImage: '../../public/saleItemImage/bigDemoImg.png', thumbnail: '../../public/saleItemImage/bigDemoImg.png' },
  { mainImage: '../../public/saleItemImage/bigDemoImg.png', thumbnail: '../../public/saleItemImage/bigDemoImg.png' },
])

</script>
 
<template>
<div v-if=" selectedItem?.id"class="font-rubik p-[5vw]">
    <button @click="goBack" class="flex items-center border-[0.17vw] border-[#6F879C] p-[1.3vw] w-fit rounded-md hover:scale-103 transition-scale ease-in-out duration-300 mb-[4vw]">
        <img src="@/assets/images/backArrow.png" alt="backArrow" class="w-[1.6vw] h-[1.3vw] mr-[0.8vw]">
        <p class="text-[#6F879C] font-semibold text-[1.3vw]">Back to product list</p>
    </button>  
    <div class="itbms-row flex justify-between">
        <div class="flex flex-col">
            <MainPhone :image="phones[selectedPhone].mainImage" />
            <div>
                <OptionsPhone
                    :phones="phones"
                    :selectedIndex="selectedPhone"
                    @update:selected-index="selectedPhone = $event"
                />
            </div>
        </div>
        <div class="flex flex-col ml-[2vw] w-[445vw]">
            <div class="border-b-[0.2vw] border-[#E5E8F4] pb-[1.5vw]">
                <p class="itbms-brand text-[#A4A4A3] text-[1.95vw]">{{ selectedItem.brandName }}</p>
                <p class="itbms-model text-[#332A1E] font-bold text-[3.5vw]">{{ selectedItem.model }}</p>
                <p class="itbms-description text-[#6F879C] text-[1.3vw]">{{ selectedItem.description }}</p>
            </div> 
            <div class="flex items-center justify-between border-b-[0.2vw] border-[#E5E8F4]">
                <p class="text-[#6F879C] pt-[1.5vw] pb-[1.5vw] font-bold text-[3.5vw]">
                    <span class="itbms-price-unit pr-[0.5vw]">฿</span>
                    <span class="itbms-price">{{ selectedItem.price?.toLocaleString() }}</span>
                </p>
                <div class="flex items-center">
                    <img src="@/assets/images/box.png" alt="box" class="w-[2.5vw] mr-[1vw]">
                    <p :class="[selectedItem.quantity > 0 ? 'bg-[#97C5B8] text-[#225528]' : 'bg-[#EAA9A9] text-[#680D0D]']"
                        class="p-[0.5vw] font-medium text-[1.5vw] rounded-full px-[2.5vw]">
                        <span class="itbms-quantity mr-[0.5vw]">{{ selectedItem.quantity }}</span>
                        <span class="itbms-quantity-unit">items in stock</span>
                    </p>
                </div>
            </div>
            <div class="text-[#332A1E] pb-[1.5vw] border-b-[0.2vw] border-[#E5E8F4]">
                <p class="pt-[1.5vw] pb-[1.5vw] font-bold text-[1.8vw]">Product Description</p>
                <div class="flex items-center justify-between text-[1.5vw]">
                    <p class="font-medium">Brand</p>
                    <p>{{ selectedItem.brandName }}</p>
                </div>
            </div>
            <ItemDetailRow label="Model" :value="selectedItem.model" />
            <ItemDetailRow label="StorageGb" :value="selectedItem.storageGb" unit="GB" valueClass="itbms-storageGb" unitClass=" itbms-storageGb-unit"/>
            <ItemDetailRow label="RamGb" :value="selectedItem.ramGb" unit="GB" valueClass=" itbms-ramGb" unitClass="itbms-ramGb-unit"/>
            <ItemDetailRow label="ScreenSizeInch" :value="selectedItem.screenSizeInch" unit="Inch" valueClass="itbms-screenSizeInch" unitClass="itbms-screenSizeInch-unit"/>
            <ItemDetailRow label="Color" :value="selectedItem.color" valueClass="itbms-color"/>
        </div>
    </div>

</div>
<div v-else class="flex flex-col md:flex-row items-center justify-center h-screen bg-white px-[2vw] font-rubik">
    <img src="../assets/product-not-found.png" alt="Product Not Found" class="w-[48vw] h-auto mb-[4vw] md:mb-0 md:mr-[5vw]"/>

    <div class="text-center md:text-left">
      <h1 class="text-[3.9vw] font-bold text-[#332A1E] leading-tight">
        <span>Product</span><br />
        <span>Not Found</span>
      </h1>
      <p class="itbms-message text-[1.65vw] text-[#332A1E] mt-[1.0vw]">The requested sale item does not exist.</p>

      <div class="flex flex-col md:flex-row gap-[1.5vw] mt-[1.75vw]">
        <button
          @click="$router.push('/')"
          class="itbms-button flex items-center border-[0.17vw] border-[#6F879C] bg-[#6F879C]  p-[0.8vw] w-fit rounded-lg hover:scale-103 transition-scale ease-in-out duration-300"
        >
          <img src="@/assets/images/home.png" alt="backArrow" class="w-[1.8vw] h-[1.9vw] mr-[0.5vw]"/>
          <p class="text-[#F2EDEC] font-semibold text-[1.2vw]">Back to homepage</p>
        </button>

        <button
          @click="$router.push('/sale-items')"
          class="itbms-button flex items-center border-[0.15vw] border-[#6F879C] p-[1.2vw] w-fit rounded-lg hover:scale-103 transition-scale ease-in-out duration-300"
        >
          <img src="@/assets/images/backArrow.png" alt="home" class="w-[1.6vw] h-[1.3vw] mr-[0.8vw]"/>
          <p class="text-[#6F879C] font-semibold text-[1.2vw]">Back to product list</p>
        </button>
      </div>
    </div>
  </div>
</template>
 
<style scoped>

</style>
