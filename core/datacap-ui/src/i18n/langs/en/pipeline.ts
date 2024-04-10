export default {
    common: {
        list: 'Pipeline List',
        logger: 'Pipeline Logger',
        loggerInfo: 'Pipeline [ $VALUE ] Logger',
        delete: 'Delete Pipeline',
        deleteInfo: 'Delete Pipeline [ $VALUE ]',
        stop: 'Stop Pipeline',
        stopInfo: 'Stop Pipeline [ $VALUE ]',
        flow: 'Pipeline Flow',
        flowInfo: 'Pipeline [ $VALUE ] Flow',
        create: 'Create Pipeline',
        input: 'Input Source',
        output: 'Output Source',
        resetTransform: 'Reset Transform',
    },
    validator: {
        from: 'Please configure the input source information',
        to: 'Please configure the output source information',
        edge: 'Please connect the input and output source'
    },
    tip: {
        deleteSuccess: 'Delete pipeline [ $VALUE ] successfully',
        deleteAlert1: 'You are deleting a pipeline. This action permanently deletes the pipeline. Please be sure to confirm your actions before proceeding.',
        deleteAlert2: 'Warning: This cannot be undone.',
        deleteAlert3: 'To confirm, type [ $VALUE ] in the box below',
        stopAlert1: 'You are stopping a pipeline. This action permanently stops the pipeline. Please be sure to confirm your actions before proceeding.',
        stopAlert2: 'Warning: This cannot be undone.',
        stopAlert3: 'To confirm, type [ $VALUE ] in the box below',
        stopSuccess: 'Pipeline [ $VALUE ] stopped successfully',
        publishSuccess: 'Pipeline [ $VALUE ] published successfully',
    }
}