import MenuChoice from "./MenuChoice";
import Resource from "./Resource";

type Metric = {
    id: number
    answerFound: boolean,
    timestamp: string | null,
    ticketLink: string | null,
    userName: string | null,
    userFeedback: string | null,
    menuchoiceIds: number[],
    resourceIds: number[]
}

export default Metric;