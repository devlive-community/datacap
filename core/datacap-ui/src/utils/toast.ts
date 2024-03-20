import { toast } from '@/components/ui/toast'

export class ToastUtils
{
    /**
     * Display an error message using a toast notification.
     *
     * @param {string} message - the error message to display
     * @return {void}
     */
    public static error(message: string): void
    {
        toast({
            description: message,
            variant: 'destructive'
        })
    }

    /**
     * Display a success message using a toast notification.
     *
     * @param {string} message - the success message to display
     * @return {void}
     */
    public static success(message: string): void
    {
        toast({
            description: message
        })
    }
}
