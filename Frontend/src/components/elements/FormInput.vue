<script setup>
defineProps({
    label: {
        type: String,
        required: true
    },
    required: Boolean,
    inputType: {
        type: String,
        required: true
    },
    placeholder: String,
    className: String,
    isNull: Boolean,
    maxlength: Number
})

const inputValue = defineModel()
</script>

<template>
    <div class="font-rubik flex flex-col">
        <label class="text-[#332A1E] font-medium text-lg">{{ label }}
            <span v-if="required === true" class="text-red-700">*</span>
            <span v-if="isNull" class=" text-xs ml-3 font-normal text-red-400">This field is required</span>
        </label>
        <textarea v-if="inputType === 'textarea'" v-model="inputValue" :placeholder="placeholder" @change="inputValue = inputValue?.trim()" :maxlength="maxlength"
            :class="[
                `${className} appearance-none py-3 w-full text-base text-[#332A1E]/80 border-[#332A1E]/20 bg-white border rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF] min-h-[6rem]`,
                { ' border-red-400' : isNull }
            ]"/>
        <input v-else :type="inputType" :required="required" v-model="inputValue" :placeholder="placeholder" @change="inputValue = inputValue?.trim()" :maxlength="maxlength"
            :class="[
                `${className} h-[2.75rem] appearance-none w-full text-base bg-white text-[#332A1E]/80 border-[#332A1E]/20 border rounded-xs px-5 mt-1 focus:outline-none focus:ring-2 focus:ring-[#2684FF]`,
                { ' border-red-400' : isNull }
            ]"/>
    </div>
</template>

<style scoped></style>