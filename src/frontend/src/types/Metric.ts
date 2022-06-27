import MenuChoice from "./MenuChoice";
import Resource from "./Resource";

type Metric = {
    id: number
    answerFound: boolean,
    timestamp: string,
    ticketLink: string,
    userName: string,
    userFeedback: string,
    menuChoices: MenuChoice[],
    resources: Resource[]
}

export default Metric;