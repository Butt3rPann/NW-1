<script setup>
import { ref, onMounted, watchEffect } from 'vue'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import { getItems } from '@/libs/fetchUtils';
import FormSelect from '@/components/elements/FormSelect.vue'
import FormInput from '@/components/elements/FormInput.vue'
import { useRouter } from 'vue-router';
import BaseButton from '../elements/BaseButton.vue';

const props = defineProps({
    saleItemData: Object,
    pathName: String,
    params: String
})

const router = useRouter()
const emit = defineEmits(['submitAction'])
const brands = ref([])
const oldSaleItem = ref(null)

onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)

        const brandObj = brands.value.find(b => b.name === props.saleItemData?.brandName)
        newSaleItem.value.brand = brandObj || newSaleItem.value.brand

        oldSaleItem.value = {...newSaleItem.value}
    } catch (error) {
        console.log(error);
    }
})

const newSaleItem = ref({
    model: props.saleItemData?.model || '',
    brand: {
        id: null,
        name: null
    },
    description: props.saleItemData?.description || '',
    price: props.saleItemData?.price || '',
    ramGb: props.saleItemData?.ramGb || '',
    screenSizeInch: props.saleItemData?.screenSizeInch || '',
    quantity: props.saleItemData?.quantity || '',
    storageGb: props.saleItemData?.storageGb || '',
    color: props.saleItemData?.color || ''
})

const disabled = ref(true)
const isNull = ref({
    model: false,
    brand: false,
    description: false,
    price: false
})

watchEffect(() => {    
    for (const key in isNull.value) {
      if (key === 'brand')
        isNull.value.brand = !newSaleItem.value.brand?.id
      else
        isNull.value[key] = !newSaleItem.value[key]
    }
    
    const hasEmptyField = Object.values(isNull.value).some(value => value === true)
    const unchanged = JSON.stringify(newSaleItem.value) === JSON.stringify(oldSaleItem.value)

    disabled.value = hasEmptyField || unchanged
})

function handleClick() {
    Object.keys(newSaleItem.value).forEach(key => {
        if (newSaleItem.value[key] === '')
            delete newSaleItem.value[key]
    })

    disabled.value = true
    emit('submitAction', newSaleItem.value)
}

const cancel = () => {
    if (props.params) {
        router.push({ name: props.pathName, params: { saleItemId: props.params } })
    } else {
        router.push({ name: props.pathName })
    }
    
}

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
    <div class="flex gap-15">
        <div class="flex flex-col items-center">
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
                        <FormSelect v-model="newSaleItem.brand" label="Brand" :options="brands" property="name"
                            placeholder="Select brand" className="itbms-brand"></FormSelect>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.model" label="Model" :required="true" inputType="text"
                            placeholder="Enter model" className="itbms-model" :maxlength="60"></FormInput>
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.price" label="Price (Baht)" :required="true" inputType="Number"
                            placeholder="Enter price" className="itbms-price"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.quantity" label="Quantity" inputType="Number"
                            placeholder="Enter quantity" className="itbms-quantity"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.description" label="Description" :required="true"
                        inputType="textarea" placeholder="Enter product description" className="itbms-description"></FormInput>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.ramGb" label="RAM (GB)" inputType="Number"
                            placeholder="Enter RAM" className="itbms-ramGb"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.storageGb" label="Storage (GB)" inputType="Number"
                            placeholder="Enter storage" className="itbms-storageGb"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.screenSizeInch" label="Screen Size (Inches)" inputType="Number"
                            placeholder="Enter screen size" className="itbms-screenSizeInch"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.color" label="Color" inputType="text" placeholder="Enter color"
                        className="itbms-color"></FormInput>
                </div>
                <div class="flex gap-4 pt-2">
                    <BaseButton @click="handleClick" text="Save" textColor="text-white" bgColor="bg-[#6F879C]" class="itbms-save-button" :disabled="disabled"/>
                    <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button"/>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>
