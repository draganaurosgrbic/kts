export interface CulturalOffer{
    id: number;
    category: string;
    type: string;
    placemarkIcon: string;
    name: string;
    location: string;
    lat: number;
    lng: number;
    description: string;
    image: string;
    totalRate: number;
    followed: boolean;
}
