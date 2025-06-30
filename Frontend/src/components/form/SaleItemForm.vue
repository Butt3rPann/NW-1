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

const isNull = ref({
    model: false,
    brand: false,
    description: false,
    price: false
})

const invalidMessage = {
    model: "Model must be 1-60 characters long.",
    brand: "Brand must be selected.",
    description: "Description must be 1-16,384 characters long.",
    price: "Price must be non-negative integer.",
    ramGb: "RAM size must be positive integer or not specified.",
    screenSizeInch: "Screen size must be positive number with at most 2 decimal points or not specified.",
    storageGb: "Storage size must be positive integer or not specified.",
    color: "Color must be 1-40 characters long or not specified.",
    quantity: "Quantity must be non-negative integer."
}

const invalid = ref({
    model: false,
    brand: false,
    description: false,
    price: false,
    ramGb: false,
    screenSizeInch: false,
    storageGb: false,
    color: false,
    quantity: false
})

const handleDisabledButton = (field, value) => {
    invalid.value[field] = value
}

const disabled = ref(true)

watchEffect(() => {    
    for (const key in isNull.value) {
      if (key === 'brand')
        isNull.value.brand = !newSaleItem.value.brand?.id
      else
        isNull.value[key] = !newSaleItem.value[key]
    }
    
    const anyInvalid = Object.values(invalid.value).some(value => value === true)
    const hasEmptyField = Object.values(isNull.value).some(value => value === true)
    const unchanged = JSON.stringify(newSaleItem.value) === JSON.stringify(oldSaleItem.value)

    disabled.value = hasEmptyField || unchanged || anyInvalid
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
</script>

<template>
    <div class="flex flex-col xl:flex-row gap-4 lg:gap-8 xl:gap-12 justify-between p-4 lg:p-8">
        <div class="flex flex-col items-center w-full xl:w-[35%] 2xl:w-[40%]">
            <div class="bg-[#F0EDEC] w-full max-w-[15rem] md:max-w-[20rem] lg:max-w-[23rem] xl:max-w-[20rem] 2xl:max-w-[24rem] aspect-[9/10] rounded-2xl flex items-center justify-center mb-3 md:mb-5">
                <img :src="phones.mainImage" alt="Selected Phone" class="h-[10rem] md:h-[13rem] lg:h-[16rem] xl:h-[16rem] 2xl:h-[18rem] object-contain">
            </div>
            <div>
                <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                    @update:selected-index="changeMainImg" />
            </div>
        </div>
        <div class="w-full xl:w-[60%] 2xl:w-[55%] space-y-4 sm:space-y-5 lg:space-y-6">
            <div class="grid gap-4 sm:gap-5 lg:gap-6">
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 lg:gap-5">
                    <div class="grid gap-1.5">
                    	<FormSelect v-model="newSaleItem.brand" label="Brand" :options="brands.sort((a, b) => a.name.localeCompare(b.name))" property="name" field="brand"
                            placeholder="Select brand" className="itbms-brand" :invalidMessage="invalidMessage.brand" @disabledButton="handleDisabledButton"></FormSelect>
		            </div>
                    <div class="grid gap-1.5">
                    	<FormInput v-model="newSaleItem.model" label="Model" :required="true" inputType="text" :maxlength="60" field="model"
                            placeholder="Enter model" className="itbms-model" :invalidMessage="invalidMessage.model" @disabledButton="handleDisabledButton"></FormInput>
		            </div>
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 sm:gap-4 lg:gap-5">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.price" label="Price (Baht)" :required="true" inputType="Number" :min="0" field="price"
                            placeholder="Enter price" className="itbms-price" :invalidMessage="invalidMessage.price" @disabledButton="handleDisabledButton"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.quantity" label="Quantity" inputType="Number" :min="0" field="quantity"
                            placeholder="Enter quantity" className="itbms-quantity" :invalidMessage="invalidMessage.quantity" @disabledButton="handleDisabledButton"></FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.description" label="Description" :required="true" :maxlength="16384" field="description"
                        inputType="textarea" placeholder="Enter product description" className="itbms-description" :invalidMessage="invalidMessage.description" @disabledButton="handleDisabledButton"></FormInput>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.color" label="Color" inputType="text" placeholder="Enter color" :maxlength="40" field="color"
                        className="itbms-color" @disabledButton="handleDisabledButton" :invalidMessage="invalidMessage.color"></FormInput>
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 sm:gap-4 lg:gap-5">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.ramGb" label="RAM (GB)" inputType="Number" :min="1" field="ramGb"
                            placeholder="Enter RAM" className="itbms-ramGb" :invalidMessage="invalidMessage.ramGb" @disabledButton="handleDisabledButton"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.storageGb" label="Storage (GB)" inputType="Number" :min="1" field="storageGb"
                            placeholder="Enter storage" className="itbms-storageGb" :invalidMessage="invalidMessage.storageGb" @disabledButton="handleDisabledButton"></FormInput>
                    </div>
                    <div class="grid gap-1.5 sm:col-span-2 lg:col-span-1">
                        <FormInput v-model="newSaleItem.screenSizeInch" label="Screen Size (Inches)" inputType="Number" :min="0.01" :max="99.99" :step="0.01" field="screenSizeInch"
                            placeholder="Enter screen size" className="itbms-screenSizeInch" :invalidMessage="invalidMessage.screenSizeInch" @disabledButton="handleDisabledButton"></FormInput>
                    </div>
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
