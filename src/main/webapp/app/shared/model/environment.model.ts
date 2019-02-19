export interface IEnvironment {
    id?: string;
    name?: string;
    url?: string;
    user?: string;
    pass?: string;
}

export class Environment implements IEnvironment {
    constructor(public id?: string, public name?: string, public url?: string, public user?: string, public pass?: string) {}
}
