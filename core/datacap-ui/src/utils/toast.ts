import { toast } from '@/components/ui/toast'

export class ToastUtils
{
    /**
     * Display an error message using a toast notification.
     *
     * @param {string} message - the error message to display
     * @return {void}
     */
    public static error(message: string)
    {
        toast({
            description: message,
            variant: 'destructive'
        })
    }
}
