
type MetricDTO = {
    answerFound: boolean,
    timestamp: string | null,
    ticketLink: string | null,
    userName: string | null,
    userFeedback: string | null,
    menuchoiceIds: number[],
    resourceIds: number[]
}

export default MetricDTO;