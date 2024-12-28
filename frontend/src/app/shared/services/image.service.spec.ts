import { TestBed } from '@angular/core/testing';
import { ImageService } from './image.service';

describe('ImageService', () => {
  let service: ImageService;
  const base64Mock = 'data:image/jpeg;base64,aGV5IHRoZXJl';
  const byteArray: Uint8Array = new Uint8Array([104, 101, 121, 32, 116, 104, 101, 114, 101]);
  const blobMock = new Blob([byteArray], {type: 'image/jpeg'});

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should convert', done => {
    let base64: string;
    service.getBase64(blobMock).subscribe(
      (path: string) => {
        base64 = path;
        expect(base64).toBe(base64Mock);
        done();
      }
    );
  });

});
