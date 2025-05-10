<script setup>
import { ref, onMounted } from 'vue'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import { getItems } from '@/libs/fetchUtils';
import FormSelect from '@/components/elements/FormSelect.vue'
import FormInput from '@/components/elements/FormInput.vue'
import FormButton from '@/components/elements/FormButton.vue'
import { useRouter } from 'vue-router';

const router = useRouter()

const emit = defineEmits(['submitAction'])

const props = defineProps({
    saleItemData: Object,
    disabled: Boolean,
    path: String
})

const newSaleItem = ref({
    model: props.saleItemData?.model || null,
    brand: {
        id: null,
        name: null
    },
    description: props.saleItemData?.description || null,
    price: props.saleItemData?.price || null,
    ramGb: props.saleItemData?.ramGb || null,
    screenSizeInch: props.saleItemData?.screenSizeInch || null,
    quantity: props.saleItemData?.quantity || null,
    storageGb: props.saleItemData?.storageGb || null,
    color: props.saleItemData?.color || null
})

const isNull = ref({
    model: false,
    brand: false,
    description: false,
    price: false,
    quantity: false
})

function handleClick() {
    Object.keys(newSaleItem.value).forEach(key => {
        if (newSaleItem.value[key] === null) {
            delete newSaleItem.value[key]
        }
    })

    for (const key in isNull.value) {
        if (key === 'brand') isNull.value.brand = !newSaleItem.value.brand.id
        else isNull.value[key] = !newSaleItem.value[key]
    }

    if (Object.values(isNull.value).every(value => value === false))
        emit('submitAction', newSaleItem.value)
}

const cancel = () => {
    router.push({ path: props.path })
}

const brands = ref([])

onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)

        const brandObj = brands.value.find(b => b.name === props.saleItemData?.brandName)
        newSaleItem.value.brand = brandObj || newSaleItem.value.brand
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
    <div class="flex justify-between items-center p-[3rem] pt-[7rem] mt-5 mb-2">
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
                        <FormSelect v-model="newSaleItem.brand" label="Brand" :options="brands" property="name"
                            placeholder="Select brand" className="itbms-brand" :isNull="isNull.brand"></FormSelect>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.model" label="Model" :required="true" inputType="text"
                            placeholder="Enter model" className="itbms-model" :isNull="isNull.model" :maxlength="60"></FormInput>
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.price" label="Price (Baht)" :required="true" inputType="Number"
                            placeholder="Enter price" className="itbms-price" :isNull="isNull.price"></FormInput>
                    </div>
                    <div class="grid gap-1.5">
                        <FormInput v-model="newSaleItem.quantity" label="Quantity" :required="true" inputType="Number"
                            placeholder="Enter quantity" className="itbms-quantity" :isNull="isNull.quantity">
                        </FormInput>
                    </div>
                </div>
                <div class="grid gap-1.5">
                    <FormInput v-model="newSaleItem.description" label="Description" :required="true"
                        inputType="textarea" placeholder="Enter product description" className="itbms-description"
                        :isNull="isNull.description"></FormInput>
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
                    <FormButton @click="handleClick" text="Save" bgColor="bg-[#6F879C]" textColor="text-white"
                        className="itbms-save-button" :disabled="disabled"></FormButton>
                    <FormButton @click="cancel" text="Cancel" bgColor="transparent" textColor="text-[#6F879C]"
                        className="itbms-cancel-button" borderColor="border-2 border-[#6F879C]"></FormButton>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>