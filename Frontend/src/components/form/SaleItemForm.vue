<script setup>
import { ref, watch, onMounted } from 'vue'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import { getSaleItems } from '@/libs/fetchUtils';
import FormSelect from '@/components/elements/FormSelect.vue'
import FormInput from '@/components/elements/FormInput.vue'
import FormButton from '@/components/elements/FormButton.vue'
import { useRouter } from 'vue-router';

const router = useRouter()

const props = defineProps({
    submitAction: Function,
    saleItemData: Object
})

const newSaleItem = ref({
    model: props.saleItemData?.model || '',
    brandName: props.saleItemData?.brandName || '',
    description: props.saleItemData?.description || '',
    price: props.saleItemData?.price || '',
    ramGb: props.saleItemData?.ramGb || '',
    screenSizeInch: props.saleItemData?.screenSizeInch || '',
    quantity: props.saleItemData?.quantity || '',
    storageGb: props.saleItemData?.storageGb || '',
    color: props.saleItemData?.color || ''
})

const cancel = () => {
    router.back()
}

watch(() => props.saleItemData, (newData) => {
    if (newData) {
        newSaleItem.value = {
            model: newData?.model || '',
            brandName: newData?.brandName || '',
            description: newData?.description || '',
            price: newData?.price || '',
            ramGb: newData?.ramGb || '',
            screenSizeInch: newData?.screenSizeInch || '',
            quantity: newData?.quantity || '',
            storageGb: newData?.storageGb || '',
            color: newData?.color || ''
        }
    }
}, { immediate: true })

const brands = ref([])

const handleSubmit = () => {
    props.submitAction?.(newSaleItem.value)
}

onMounted(async () => {
    try {
        brands.value = await getSaleItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
        
    } catch (error) {
        console.log(error);
    }
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
    <div class="flex justify-between items-center p-[3rem] pt-[7rem]">
        <div class="flex flex-col items-center mr-[1.7vw]">
            <div class="bg-[#F0EDEC] w-[30vw] h-[33vw] rounded-2xl flex items-center justify-center">
                <img :src="phones.mainImage" alt="Selected Phone" class="h-[24vw]">
            </div>
            <div>
                <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                    @update:selected-index="changeMainImg" />
            </div>
        </div>
        <div class="space-y-3">
            <div class="grid gap-5">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormSelect v-model="newSaleItem.brandName" label="Brand" :options="brands" property="name" placeholder="Select brand" className="itbms-brand"></FormSelect>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.model" label="Model" :required="true" inputType="text" placeholder="Enter model" className="itbms-model"></FormInput>
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.price" label="Price (Baht)" :required="true" inputType="Number" placeholder="Enter price" className="itbms-price"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.quantity" label="Quantity" :required="true" inputType="Number" placeholder="Enter quantity" className="itbms-quantity"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.description" label="Description" :required="true" inputType="textarea" placeholder="Enter product description" className="itbms-description"></FormInput>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.ramGb" label="RAM (GB)" :required="false" inputType="Number" placeholder="Enter RAM" className="itbms-ramGb"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.storageGb" label="Storage (GB)" :required="false" inputType="Number" placeholder="Enter storage" className="itbms-storageGb"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.screenSizeInch" label="Screen Size (Inches)" :required="false" inputType="Number" placeholder="Enter screen size" className="itbms-screenSizeInch"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.color" label="Color" :required="false" inputType="text" placeholder="Enter color" className="itbms-color"></FormInput>
                </div>
                <div class="flex gap-4 pt-2">
                    <FormButton @click="handleSubmit" text="Save" bgColor="bg-[#6F879C]" textColor="text-white" className="itbms-save-button"></FormButton>
                    <FormButton @click="cancel" text="Cancel" bgColor="transparent" textColor="text-[#6F879C]" className="itbms-cancel-button" borderColor="border-2 border-[#6F879C]"></FormButton>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>