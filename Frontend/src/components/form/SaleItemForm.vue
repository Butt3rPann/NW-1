<script setup>
import { ref, onMounted, watchEffect } from 'vue'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import { getItems } from '@/libs/fetchUtils'
import FormSelect from '@/components/elements/FormSelect.vue'
import FormInput from '@/components/elements/FormInput.vue'
import { useRouter } from 'vue-router'
import BaseButton from '@/components/elements/BaseButton.vue'

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
            newSaleItem.value[key] = null
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
    mainImage: '/nw1/saleItemImage/demoImg1.png',
    thumbnail: [
        '/nw1/saleItemImage/demoImg1.png',
        '/nw1/saleItemImage/demoImg2.png',
        '/nw1/saleItemImage/demoImg3.png',
        '/nw1/saleItemImage/demoImg4.png'
    ]
})

const selectedPhone = ref(0)

const changeMainImg = (index) => {
    selectedPhone.value = index
    phones.value.mainImage = phones.value.thumbnail[selectedPhone.value]
}

const invalid = ref({
    model: "Model must be 1-60 characters long.",
    brand: "Brand must be selected.",
    description: "Description must be 1-65,535 characters long.",
    price: "Price must be non-negative integer.",
    ramGb: "RAM size must be positive integer or not specified.",
    screenSizeInch: "Screen size must be positive number with at most 2 decimal points or not specified.",
    storageGb: "Storage size must be positive integer or not specified.",
    color: "Color must be 1-40 characters long or not specified.",
    quantity: "Quantity must be non-negative integer."
})

const disabledSaveBtn = () => {
  disabled.value = true
}
</script>

<template>
    <div class="flex flex-col lg:flex-row gap-8  lg:gap-10 justify-between">
        <div class="flex flex-col items-center lg:w-[35%] xl:w-[45%]">
            <div class="bg-[#F0EDEC] w-full max-w-[24rem] sm:w-[17rem] md:w-[22rem] lg:w-[20rem] xl:w-[24rem] aspect-[9/10] rounded-2xl flex items-center justify-center">
                <img :src="phones.mainImage" alt="Selected Phone" class="h-[13rem] sm:h-[13rem] md:h-[33vw] lg:h-[22vw] xl:h-[18rem]">
            </div>
            <div>
                <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                    @update:selected-index="changeMainImg" />
            </div>
        </div>
        <div class="space-y-3 lg:w-[55%]">
            <div class="grid gap-5">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormSelect v-model="newSaleItem.brand" label="Brand" :options="brands.sort((a, b) => a.name.localeCompare(b.name))" property="name"
                            placeholder="Select brand" className="itbms-brand" :invalidMessage="invalid.brand" @disabledButton="disabledSaveBtn"></FormSelect>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.model" label="Model" :required="true" inputType="text" :maxlength="60"
                            placeholder="Enter model" className="itbms-model" :invalidMessage="invalid.model" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.price" label="Price (Baht)" :required="true" inputType="Number" :min="0" 
                            placeholder="Enter price" className="itbms-price" :invalidMessage="invalid.price" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.quantity" label="Quantity" inputType="Number" :min="0"
                            placeholder="Enter quantity" className="itbms-quantity" :invalidMessage="invalid.quantity" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.description" label="Description" :required="true" :maxlength="65535"
                        inputType="textarea" placeholder="Enter product description" className="itbms-description" :invalidMessage="invalid.description" @disabledButton="disabledSaveBtn"></FormInput>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.ramGb" label="RAM (GB)" inputType="Number" :min="1"
                            placeholder="Enter RAM" className="itbms-ramGb" :invalidMessage="invalid.ramGb" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.storageGb" label="Storage (GB)" inputType="Number" :min="1"
                            placeholder="Enter storage" className="itbms-storageGb" :invalidMessage="invalid.storageGb" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.screenSizeInch" label="Screen Size (Inches)" inputType="Number" :min="1" :max="9.99" :step="0.01"
                            placeholder="Enter screen size" className="itbms-screenSizeInch" :invalidMessage="invalid.screenSizeInch" @disabledButton="disabledSaveBtn"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.color" label="Color" inputType="text" placeholder="Enter color" :maxlength="40"
                        className="itbms-color" @disabledButton="disabledSaveBtn"></FormInput>
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
